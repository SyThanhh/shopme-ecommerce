package com.shopme.admin.shippingrate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.swing.Spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTest {

	@MockBean private ShippingRateRepository shipRepo;
	
	@MockBean private ProductRepository productRepo;
	
	
	@InjectMocks
	private ShippingRateService shipService;
	
	@Test
	private void testCalculateShippingCost_notfound() {
		Integer productId = 1;
		Integer countryId = 234;
		
		String state = "Đồng Tháp";
		
		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);
		
		assertThrows(ShippingRateAlreadyExistsException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				// TODO Auto-generated method stub
				shipService.calculateShippingCost(productId, countryId, state);
			}
		});
	}
	
	@Test
	private void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
		Integer productId = 1;
		Integer countryId = 234;
		
		String state = "New York";
		
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10);
		
		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(shippingRate);
		
		Product product = new Product();
		product.setLength(5);
		product.setWidth(4);
		product.setWeight(3);
		product.setHeight(8);
		
		Mockito.when(productRepo.findById(productId)).thenReturn(Optional.of(product));
		
		float shippingCost = shipService.calculateShippingCost(productId, countryId, state);
		
		
		assertEquals(50, shippingCost);
	}
	
}
