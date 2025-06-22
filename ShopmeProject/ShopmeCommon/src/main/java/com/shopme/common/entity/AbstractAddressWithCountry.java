package com.shopme.common.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAddressWithCountry extends AbstractAddress {

	

	@ManyToOne
	@JoinColumn(name = "country_id")
	protected Country country;
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	
	private boolean isNotEmpty(String str) {
	    return str != null && !str.trim().isEmpty();
	}
	
	
	
	
	
	
	@Override
	public String toString() {
		StringBuilder address = new StringBuilder();

	    if (isNotEmpty(firstName)) address.append(firstName);
	    if (isNotEmpty(lastName)) address.append(" ").append(lastName);
	    if (isNotEmpty(addressLine1)) address.append(", ").append(addressLine1);
	    if (isNotEmpty(addressLine2)) address.append(", ").append(addressLine2);
	    if (isNotEmpty(city)) address.append(", ").append(city);
	    if (isNotEmpty(state)) address.append(" ").append(state);
	    if (country != null && isNotEmpty(country.getName())) address.append(" ").append(country.getName());
	    if (isNotEmpty(postalCode)) address.append(". Postal Code ").append(postalCode);
	    if (isNotEmpty(phoneNumber)) address.append(". Phone number ").append(phoneNumber);

	    return address.toString().trim();
	}
}
