package com.project.gryllo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/", "/user/**", "/follow/**", "/image/**", "/chat","/chat/**")
		.authenticated()
		.antMatchers("/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest()
		.permitAll()
		.and()
		.formLogin()
		.loginPage("/auth/loginForm")
		.loginProcessingUrl("/auth/loginProcess")
		.defaultSuccessUrl("/")
		.and()
		.logout()
		.logoutUrl("/auth/logout")
		.logoutSuccessUrl("/auth/loginForm")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID");
	}
}







