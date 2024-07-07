package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncoderPassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String pw = "nam123456";
		String encoderPassword = bCryptPasswordEncoder.encode(pw);
		System.out.println(encoderPassword);
		
		boolean matches= bCryptPasswordEncoder.matches(pw, encoderPassword);
		
		assertThat(matches).isTrue();
	}
}
