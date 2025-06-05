package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private CartItemRepository cartRepo;
	
	@Autowired
	private ProductRepository produtcRepo;
	
	public Integer addProduct(Integer productId, Integer quantity , Customer customer) throws ShoppingCartException {
		
		Integer updatedQuantity = quantity;
		
		Product product = new Product(productId);
		
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		if(cartItem !=  null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Không thể thêm sản phẩm với số lượng > 6 "
						+ "vì trong giỏ hàng bạn đã có sẵn ");
			} else {
				
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
			
		}
		
		cartItem.setQuantity(updatedQuantity);
		
		cartRepo.save(cartItem);
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer) {
		return cartRepo.findByCustomer(customer);
		
	}
	
	public float updateQuantity(Integer produtcId , Integer quantity, Customer customer) {
		cartRepo.updateQuantity(quantity, customer.getId(), produtcId);
		Product product = produtcRepo.findById(produtcId).get();
		
		float subtotal = product.getDiscountPrice() * quantity;
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
}
