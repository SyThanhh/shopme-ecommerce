package com.shopme.admin;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MessageAlertCheckNull<T> {

	
	public static<T> void alertCheckNull(T item, RedirectAttributes redirectAttributes) {
		if(item != null) {
			redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
			redirectAttributes.addFlashAttribute("typeAlert", "success");
		} else {
			redirectAttributes.addFlashAttribute("message", "The user has been saved falsely");
			redirectAttributes.addFlashAttribute("typeAlert", "danger");
		}
	}
}
