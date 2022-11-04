package org.ae.account.management.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ae.account.management.security.jwt.JWTConfigurer;
import org.ae.account.management.security.jwt.TokenProvider;
import org.ae.account.management.util.AuthoritiesConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@Slf4j
@Import(SecurityProblemSupport.class)
public class SecurityConfig {

  private final TokenProvider tokenProvider;
  private final CorsFilter corsFilter;
  private final SecurityProblemSupport problemSupport;

  @Bean
  public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf()
      .ignoringAntMatchers("/h2-console/**")
      .disable()
      .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling()
      .authenticationEntryPoint(problemSupport)
      .accessDeniedHandler(problemSupport)
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .antMatchers("/swagger-ui/**").permitAll()
      .antMatchers("/test/**").permitAll()
      .antMatchers("/h2-console/**").permitAll()
      .antMatchers("/api/authenticate").permitAll()
      .antMatchers("/api/register").permitAll()
      .antMatchers("/api/activate").permitAll()
      .antMatchers("/api/account/reset-password/init").permitAll()
      .antMatchers("/api/account/reset-password/finish").permitAll()
      .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
      .antMatchers("/api/**").authenticated()
      .antMatchers("/websocket/**").authenticated()
      .antMatchers("/management/health").permitAll()
      .antMatchers("/management/health/**").permitAll()
      .antMatchers("/management/info").permitAll()
      .antMatchers("/management/prometheus").permitAll()
      .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
      .and()
      .httpBasic()
      .and()
      .apply(securityConfigurerAdapter());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private JWTConfigurer securityConfigurerAdapter() {
    return new JWTConfigurer(tokenProvider);
  }

}
