package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.checkout.paypal.PayPalService;
import com.shopme.checkout.paypal.PaypalApiException;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ShippingRateService shippingRateService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SettingService settingService;
	
	
	@Autowired
	private PayPalService payPalService;
	
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
			Customer customer = getAuthenticatedCustomer(request);
			
			
			Address defaultAddress = addressService.getDefaultAddress(customer);
			ShippingRate shippingRate = null;
			boolean usePrimaryAddressAsDefault = false;
			
			if(defaultAddress != null) {
				
				model.addAttribute("shippingAddress", defaultAddress.toString());
				shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
			} else {
				
				model.addAttribute("shippingAddress", customer.toString());
				usePrimaryAddressAsDefault = true;
				shippingRate = shippingRateService.getShippingRateForCustomer(customer);
			}
			
			if(shippingRate == null) {
				return "redirect:/cart";
			}
			List<CartItem> cartItems =  shoppingCartService.listCartItems(customer);
			CheckoutInfo checkoutInfo =  checkoutService.prepareCheckout(cartItems, shippingRate);
			
			// checkout payment
			String currencyCode = settingService.getCurrencyCode();
			PaymentSettingBag paymentSetting = settingService.getPaymentSettings();
			String paypalClientId = paymentSetting.getClientID();
			
			
			model.addAttribute("paypalClientId", paypalClientId);
			model.addAttribute("customer", customer);
			model.addAttribute("currencyCode", currencyCode);
			model.addAttribute("checkoutInfo", checkoutInfo);
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("locale", "vi_VN");

			
			return "checkout/checkout";
		
		
		
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest req)  {
		String email = Utility.getMailOfAUthenticatedCustomer(req);
		
		return customerService.getCusomerByEmail(email);
	}
	
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		String paymentType = request.getParameter("paymentMethod");
		
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Customer customer = getAuthenticatedCustomer(request);
		
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		if(defaultAddress != null) {
			
			shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
		} else {
		
			shippingRate = shippingRateService.getShippingRateForCustomer(customer);
		}
		
		List<CartItem> cartItems =  shoppingCartService.listCartItems(customer);
		CheckoutInfo checkoutInfo =  checkoutService.prepareCheckout(cartItems, shippingRate);
		
		
		 Order createOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		shoppingCartService.deleteByCustomer(customer);
		
		
		sendOrderConfirmationEmail(request, createOrder);
		
		return "checkout/order_completed";
	}
	
	private void sendOrderConfirmationEmail(HttpServletRequest request , Order order) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettingBag = settingService.getEmailString();
		
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
		mailSender.setDefaultEncoding("utf-8");
		
		
		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettingBag.getOrderConfirmationSubject();
		String content = emailSettingBag.getOrderConfirmationContent();
		
		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		
		helper.setTo(toAddress);
		
		helper.setSubject(subject);
		
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String orderTime = dateFormatter.format(order.getOrderTime());
		
		CurrencySettingBag currencySetting = settingService.getCurrencySettings();
		
		String totalAmount =  Utility.formatCurrency(order.getTotal(), currencySetting );
		
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", String.valueOf(orderTime));
		content = content.replace("[[shippingAddress]]", order.getShippingAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
		
		
		helper.setText(content, true);
		mailSender.send(message);
		
		
	}
	
	@PostMapping("/process_paypal_order")
	public String processPayPalOrder(HttpServletRequest request, Model model) 
			throws UnsupportedEncodingException, MessagingException {
		String orderId = request.getParameter("orderId");
		
		String pageTitle = "Checkout Failure";
		String message = null;
		
		try {
			if (payPalService.validateOrder(orderId)) {
				return placeOrder(request);
			} else {
				pageTitle = "Checkout Failure";
				message = "ERROR: Transaction could not be completed because order information is invalid";
			}
		} catch (PaypalApiException e) {
			message = "ERROR: Transaction failed due to error: " + e.getMessage();
		}
		
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("title", pageTitle);
		model.addAttribute("message", message);
		
		return "message";
	}
}
