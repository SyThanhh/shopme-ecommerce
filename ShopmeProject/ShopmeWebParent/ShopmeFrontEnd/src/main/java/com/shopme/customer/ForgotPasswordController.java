package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.SettingBag;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class ForgotPasswordController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model )  {
		try {
				String email = request.getParameter("email");
				String token = customerService.updateResetPasswordToken(email);
			
				String link = Utility.getSiteURL(request)+ "/reset_password?token="+token;
			
				sendEmail(email, link);
		
				model.addAttribute("message", "We have sent a reset passoword link to your email. "
						+ "Please check.");
			} catch (CustomerNotFoundException e) {
				model.addAttribute("error", e.getMessage());
			} catch (UnsupportedEncodingException | MessagingException e) {
				model.addAttribute("error", "Could not send email");
			}	
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String email, String link) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailString();
		// Lấy thông tin cấu hình email (từ, tiêu đề, nội dung, v.v...) từ dịch vụ cấu hình.

		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		// Tạo đối tượng gửi email, thiết lập các thông số SMTP (host, port, username, password...) dựa vào emailSettings.
		
		String toAddress = email;
		// Lấy địa chỉ email người nhận (ở đây là email của khách hàng).

		String subject = "Here's the link to reset your password";
		
		StringBuffer content = new StringBuffer();
		content.append("<p>Hello, </p>");
		content.append("<p>You have requested to reset your password.</p>");
		content.append("<p>Click the link below to change your password:</p>");
		content.append("<p><a href=\"" + link + "\">Reset Password</a></p>");
		content.append("</br>");
		content.append("<p>Ignore this email if you do remember your password , or you have not made the request</p>");
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message);
		

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		
		helper.setTo(toAddress);
		
		helper.setSubject(subject);
		
		helper.setText(content.toString(), true);
		
		mailSender.send(message);
		
		
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);	
	
		if(customer != null) {
			model.addAttribute("token", token);
		} else {
			model.addAttribute("message", "Invalid Token");
			model.addAttribute("pageTitle", "Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
		
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			
			model.addAttribute("pageTitle", "Reset Your Password");
			model.addAttribute("title", "Reset Your Password");
			model.addAttribute("message", "You have successfully changed your password.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
		}	

		return "message";		
	}
	

}
