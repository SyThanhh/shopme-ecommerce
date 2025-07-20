package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositotyTests {
	
	@Autowired private OrderRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testAddNewOrder() {
		
		Customer customer = entityManager.find(Customer.class,1);
		Product product = entityManager.find(Product.class, 1);
		
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.setFirstName(customer.getFirstName());
		mainOrder.setLastName(customer.getLastName());
		mainOrder.setPhoneNumber(customer.getPhoneNumber());
		mainOrder.setAddressLine1(customer.getAddressLine1());
		mainOrder.setAddressLine2(customer.getAddressLine2());
		mainOrder.setCity(customer.getCity());
		mainOrder.setCountry(customer.getCountry().getName());
		mainOrder.setPostalCode(customer.getPostalCode());
		mainOrder.setState(customer.getState());
		
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10);
		
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail);
		
		Order savedOrder = repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
				
	}
	
	@Test
	public void testListOrder() {
		Iterable<Order> orders = repo.findAll();
		
		assertThat(orders).hasSizeGreaterThan(0);
		
		orders.forEach(System.out::println);
	}
	
	@Test
	public void testUpdateOrder() {
		Integer orderId = 2;
		
		Order order =  repo.findById(orderId).get();
		order.setStatus(OrderStatus.SHIPPING);
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderTime(new Date());
		order.setDeliverDays(2);
		
		Order updateOrder = repo.save(order);
		
		assertThat(updateOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
	}
	
	@Test
	public void testGetOrder() {
		Integer orderId = 2;
		
		Order order = repo.findById(orderId).get();
		
		assertThat(order).isNotNull();
	}
	
	@Test
	public void testDeleteOrder() {
		Integer orderId = 2;
		
		repo.deleteById(orderId);
		
		Optional<Order> result = repo.findById(orderId);
		
		assertThat(result).isNotPresent();
		
	}
	
	@Test
	public void testUpdateOrderStatus() {
		Integer orderId = 16;
		
		Order order = repo.findById(orderId).get();

		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setOrder(order);
		
		orderTrack.setUpdatedTime(new Date());
		orderTrack.setOrderStatus(OrderStatus.PROCESSING);
		orderTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(orderTrack);
		
		Order updateOrder = repo.save(order);
		
		assertThat(updateOrder.getOrderTracks()).hasSizeGreaterThan(1);
		
		
	}
}
