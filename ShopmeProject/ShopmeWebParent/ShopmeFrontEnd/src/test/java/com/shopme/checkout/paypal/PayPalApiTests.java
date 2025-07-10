package com.shopme.checkout.paypal;



import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {

	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AepvxiOSdJF-NhtZykG0cFFIgeaB7vqYQMmqFqtSFV9dmJ5ko4DZceMILHZAsZ8XTzuMJkhfRvs_IKbb";
	
	private static final String CLIENT_SERECT = "EDwDQ_v-dUbnBh-cTEPddS9WYsR6ppaY_aTHCvo3RJaUGF6yhKBa9kAjWb8mCBtrr5r0Z03JpRB3btE8";
	
	
	@Test
	public void testGetOrderDetails() {
	    String orderId = "1XE05904553989828";
	    String requestURL = BASE_URL + GET_ORDER_API + orderId;

	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.add("Accept-Language", "en_US");
	    headers.setBasicAuth(CLIENT_ID, CLIENT_SERECT);

	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
	    RestTemplate restTemplate = new RestTemplate();

	    ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(
	            requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	    PayPalOrderResponse palOrderResponse = response.getBody();
	    
	    
	    System.out.println("order Id :  " + palOrderResponse.getId());
	    System.out.println("validate :  " + palOrderResponse.validate(orderId));
	  
	}
	
}
