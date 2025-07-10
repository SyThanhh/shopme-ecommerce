package com.shopme.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.State;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

	
	public ShippingRate findByCountryAndState(Country country, String state);
}
