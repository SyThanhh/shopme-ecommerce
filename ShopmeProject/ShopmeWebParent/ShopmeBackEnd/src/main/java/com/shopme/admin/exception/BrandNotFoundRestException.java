package com.shopme.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Brand not found")
public class BrandNotFoundRestException extends Exception {

	public BrandNotFoundRestException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}

}
