package org.ae.account.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf()
      .ignoringAntMatchers("/h2-console/**")
      .disable()
      .authorizeRequests()
      .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .antMatchers("/swagger-ui/**").permitAll()
      .antMatchers("/test/**").permitAll()
      .antMatchers("/h2-console/**").permitAll();
    return http.build();
  }

}
