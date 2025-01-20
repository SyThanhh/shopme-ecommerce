package com.shopme.admin.security;

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

    // Định nghĩa một bean UserDetailsService để cung cấp logic nạp dữ liệu người dùng
    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDeatilsService();
    }
    
    // Định nghĩa một bean PasswordEncoder để mã hóa mật khẩu người dùng
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Định nghĩa một bean DaoAuthenticationProvider để quản lý quá trình xác thực người dùng
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenProvider = new DaoAuthenticationProvider();
        authenProvider.setUserDetailsService(userDetailsService());
        authenProvider.setPasswordEncoder(passwordEncoder());
        
        return authenProvider;
    }
    
    // Cấu hình AuthenticationManager để sử dụng DaoAuthenticationProvider để xác thực người dùng
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    // Cấu hình bảo mật HTTP
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/users/**", "/settings/**", "/countries/**","/states/**").hasAuthority("Admin")
        	.antMatchers("/categories/**", "brands/**").hasAnyAuthority("Admin", "Editor") 
        	
        	.antMatchers("/products/new", "/prodducts/delete/**")
        		.hasAnyAuthority("Admin", "Editor")
        		
        	.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
        		.hasAnyAuthority("Admin", "Editor", "Salesperson")
        		
        	.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
        		.hasAnyAuthority("Admin", "Editor","Salesperson", "Shippers")
        		
        	.antMatchers("/products/**").hasAnyAuthority("Admin",  "Edit")
            .anyRequest().authenticated() // Mọi yêu cầu đều phải xác thực
            .and()
            .formLogin()
                .loginPage("/login") // Định nghĩa trang đăng nhập tùy chỉnh
                .usernameParameter("email")
                .permitAll() // Cho phép truy cập vào trang đăng nhập mà không cần xác thực
        	.and().logout().permitAll()
        	.and()
        			.rememberMe()
        				.key("AbcDefgHijKmlnOpqrs_1234567890")
        				.tokenValiditySeconds(3 * 60 * 60);
    
    }

    // Cấu hình WebSecurity để bỏ qua xác thực cho các tài nguyên tĩnh
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/images/**", "/js/**", "/webjars/**"); // Bỏ qua xác thực cho các tài nguyên tĩnh này
    }
}
