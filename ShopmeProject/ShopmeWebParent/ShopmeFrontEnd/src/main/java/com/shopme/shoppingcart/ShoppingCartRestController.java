package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	
	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name ="productId") Integer productId, 
			@PathVariable(name="quantity") Integer quantity,
			HttpServletRequest request
			
			) throws ShoppingCartException {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			return " Đã thêm vào giỏ hàng với số lượng là " + updatedQuantity;
		}catch(CustomerNotFoundException ex) {
			return "Bạn nên đăng nhập trước khi thêm sản phẩm";
		} catch(ShoppingCartException sx) {
			return sx.getMessage();
		}
		
		
	}
	
	
	private Customer getAuthenticatedCustomer(HttpServletRequest req) throws CustomerNotFoundException {
		String email = Utility.getMailOfAUthenticatedCustomer(req);
		
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated Customer");
		}
		return customerService.getCusomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name ="productId") Integer productId, 
			@PathVariable(name="quantity") Integer quantity,
			HttpServletRequest request
			) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			return String.valueOf(subtotal);
		}catch(CustomerNotFoundException ex) {
			return "Bạn nên đăng nhập để thay đổi số lượng của sản phẩm";
		} 
		
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId
			, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			
			return "Sản phẩm đã được xóa từ giỏ hàng của bạn";
		}catch(CustomerNotFoundException ex) {
			return "Bạn nên đăng nhập vào trước khi xóa sản phẩm";
		} 
	}
}
