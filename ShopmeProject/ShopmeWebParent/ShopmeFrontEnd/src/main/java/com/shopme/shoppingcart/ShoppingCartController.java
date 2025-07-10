package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {

	
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shippingService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest req) {
		
		try {
			Customer customer = getAuthenticatedCustomer(req);
			List<CartItem> listCartItems =  cartService.listCartItems(customer);
			
			float estimatedTotal = 0.0F;
			for (CartItem cartItem : listCartItems) {
				estimatedTotal += cartItem.getSubtotal();
			}
			
			Address defaultAddress = addressService.getDefaultAddress(customer);
			ShippingRate shippingRate = null;
			boolean usePrimaryAddressAsDefault = false;
			
			if(defaultAddress != null) {
				shippingRate = shippingService.getShippingRateForAddress(defaultAddress);
			} else {
				usePrimaryAddressAsDefault = true;
				shippingRate = shippingService.getShippingRateForCustomer(customer);
			}
			
			model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
			model.addAttribute("shippingSupported", shippingRate != null);
			model.addAttribute("listCartItems", listCartItems);
			model.addAttribute("estimatedTotal", estimatedTotal);
			
			
			return "cart/shopping_cart";
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest req) throws CustomerNotFoundException {
		String email = Utility.getMailOfAUthenticatedCustomer(req);
		
		return customerService.getCusomerByEmail(email);
	}
	
	
}
