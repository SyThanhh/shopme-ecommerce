package com.shopme.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

	@Autowired private CartItemRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		CartItem cartItem = new  CartItem();
		
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 1);
		
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		cartItem.setQuantity(10);
		
		CartItem savedItem =  repo.save(cartItem);
		
		assertThat(savedItem.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testSave2Items() {
		CartItem cartItem = new  CartItem();
		
		Customer customer = entityManager.find(Customer.class, 10);
		Product product = entityManager.find(Product.class, 10);
		
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		cartItem.setQuantity(10);
		
		CartItem item2 = new CartItem();
		item2.setCustomer(new Customer(2));
		item2.setProduct(new Product(3));
		item2.setQuantity(4);

		
		Iterable<CartItem> iterable =  repo.saveAll(List.of(cartItem, item2));
		
		assertThat(iterable).size().isGreaterThan(0);
		
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 10;
		
		List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));
		
		listItems.forEach(System.out::println);
		
		assertThat(listItems).size().isEqualTo(1);
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 10;
		Integer productId = 10;
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
	
		
		assertThat(item).isNotNull();
		
	}
	
	@Test
	public void testUpdateQuantiyByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId = 1;
		
		repo.updateQuantity(100, customerId, productId);
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item.getQuantity()).isEqualTo(100);
	}
	
	@Test
	public void testDeleteQuantiyByCustomerAndProduct() {
		Integer customerId = 10;
		Integer productId = 10;
		
		repo.deleteByCustomerAndProduct(customerId, productId);
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNull();
	}
}
