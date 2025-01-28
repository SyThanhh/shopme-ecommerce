package com.shopme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity //  turn on security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    // Cấu hình bảo mật HTTP
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        	
    
    }

    // Cấu hình WebSecurity để bỏ qua xác thực cho các tài nguyên tĩnh
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/images/**", "/js/**", "/webjars/**"); // Bỏ qua xác thực cho các tài nguyên tĩnh này
    }
}
