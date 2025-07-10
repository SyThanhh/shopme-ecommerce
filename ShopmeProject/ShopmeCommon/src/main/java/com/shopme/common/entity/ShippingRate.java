package com.shopme.common.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shipping_rates")
public class ShippingRate extends IdBasedEntity {
	
	
	private float rate;
	
	private int days;
	
	@Column(name="cod_supported")
	private boolean codSupported;
	
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(nullable = false, length = 45)
	private String state;
	
	public ShippingRate() {
		// TODO Auto-generated constructor stub
	}


	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codSupported, country, days, id, rate, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingRate other = (ShippingRate) obj;
		return codSupported == other.codSupported && Objects.equals(country, other.country) && days == other.days
				&& Objects.equals(id, other.id) && Float.floatToIntBits(rate) == Float.floatToIntBits(other.rate)
				&& Objects.equals(state, other.state);
	}


	@Override
	public String toString() {
		return "ShippingRate [rate=" + rate + ", days=" + days + ", codSupported=" + codSupported + ", country="
				+ country + ", state=" + state + "]";
	}
	
	

}
