package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.oauth.CustomerOAuth2User;
import com.shopme.security.CustomerUserDetails;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listAllCountries = customerService.listAllCoutries();
		model.addAttribute("listCountries", listAllCountries);
		model.addAttribute("pageTitle", "Customer Register");
		model.addAttribute("customer", new Customer());
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		
		sendVerificationEmail(request, customer);
		
		model.addAttribute("pageTitle", "Registration Succeeded !");
		
		return "register/register_success";
	}
	
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailString();
		// Lấy thông tin cấu hình email (từ, tiêu đề, nội dung, v.v...) từ dịch vụ cấu hình.

		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		// Tạo đối tượng gửi email, thiết lập các thông số SMTP (host, port, username, password...) dựa vào emailSettings.

		String toAddress = customer.getEmail();
		// Lấy địa chỉ email người nhận (ở đây là email của khách hàng).

		String subject = emailSettings.getCustomerVerifySubject();
		// Lấy tiêu đề email (đã cấu hình trong hệ thống, ví dụ: "Xác minh tài khoản").

		String content = emailSettings.getCustomerVerifyContent();
		// Lấy nội dung email gốc, có thể chứa placeholder như [[name]], [[URL]] để thay sau.

		MimeMessage message = mailSender.createMimeMessage();
		// Tạo một đối tượng email MIME — dùng để định nghĩa email với nội dung dạng văn bản, HTML, tệp đính kèm, v.v.

		MimeMessageHelper helper = new MimeMessageHelper(message);
		// Helper giúp dễ dàng cấu hình các phần tử email như: from, to, subject, nội dung, encoding...

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		// Thiết lập người gửi email, gồm địa chỉ (From) và tên hiển thị (Sender Name).

		helper.setTo(toAddress);
		// Thiết lập địa chỉ email người nhận.

		helper.setSubject(subject);
		// Thiết lập tiêu đề (subject) của email.

		content = content.replace("[[name]]", customer.getFullName());
		// Thay thế placeholder [[name]] trong nội dung email bằng tên thật của khách hàng.

		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		// Tạo đường dẫn xác minh (verify URL) cho tài khoản người dùng. Gắn thêm mã xác minh để phân biệt người dùng.

		content = content.replace("[[URL]]", verifyURL);
		// Thay thế placeholder [[URL]] trong nội dung email bằng đường dẫn xác minh thật.

		helper.setText(content, true);
		// Thiết lập nội dung email đã thay thế. Tham số `true` nghĩa là nội dung dạng HTML.

		mailSender.send(message);
		// Gửi email đi bằng đối tượng mailSender đã cấu hình SMTP.

		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
		// In ra console địa chỉ email người nhận và đường dẫn xác minh để kiểm tra.

	}	
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		Boolean verified = customerService.verify(code);
		return "register/" + (verified ? "verify_success" : "verify_fail");
	} 
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest req) {
		String email = Utility.getMailOfAUthenticatedCustomer(req);
		Customer customer = customerService.getCusomerByEmail(email);
		
		List<Country> listCountries = customerService.listAllCoutries();
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("customer", customer);
		return "customer/account_form";
		
	}
	
	
	
	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra, HttpServletRequest req) {
		customerService.update(customer);
		
		ra.addFlashAttribute("message", "Your account details have been updated. ");
		ra.addFlashAttribute("typeAlert", "success");
		
		updateNameForAuthenticatedCustomer(customer,req);
		
		String redirectOption = req.getParameter("redirect");
		String redirectURL = "redirect:/account_details";
		
		if ("address_book".equals(redirectOption)) {
			redirectURL = "redirect:/address_book";
		} else if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		} else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/address_book?redirect=checkout";
		}
		
		return redirectURL;
	}

	private void updateNameForAuthenticatedCustomer( Customer customer, HttpServletRequest req) {
		Object principal = req.getUserPrincipal();
		String fullName = customer.getFirstName() + " " + customer.getLastName();
		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
			
		} else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			oauth2User.setFullName(fullName);
			
		}
	}
	
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		
		CustomerUserDetails userDetails = null;
		if(principal instanceof UsernamePasswordAuthenticationToken ) {
			
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails)token.getPrincipal();
		} else if(principal instanceof RememberMeAuthenticationToken) { //Đăng nhập lại bằng cookie remember-me
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails)token.getPrincipal();
		}
		
		
		
		return userDetails;
	}
	
	
}
