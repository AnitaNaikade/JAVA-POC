package com.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	// authentication
	public UserDetailsService userDetailsService() {

		return new UserInfoUserDetailsService();
	}

	// Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable().authorizeHttpRequests().requestMatchers("/products/welcome", "/products/new")
				.permitAll().and().authorizeHttpRequests().requestMatchers("/products/**").authenticated().and()
				.formLogin().and().build();
	}

	// to encode the password
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// we have define user details service that will talk with DB & validate user
	// but need one Authontication provider
	// to talk with user detail service so need this bean
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

		// we need to inform who is user details service & what is Password Encoder

		authenticationProvider.setUserDetailsService(userDetailsService()); // above Bean of userDetailsService
		authenticationProvider.setPasswordEncoder(passwordEncoder()); // above Bean of passwordEncoder
		return authenticationProvider;
	}

}

// this is Hard code authentication

/*     public UserDetailsService userDetailsService() {
  
  UserDetails admin = User.withUsername("Basant")
.password(encoder.encode("Pwd1"))
 .roles("ADMIN")
 .build();
UserDetails user = User.withUsername("John")
 .password(encoder.encode("Pwd2"))
 .roles("USER","ADMIN","HR")
 .build();
return new InMemoryUserDetailsManager(admin, user);*/
