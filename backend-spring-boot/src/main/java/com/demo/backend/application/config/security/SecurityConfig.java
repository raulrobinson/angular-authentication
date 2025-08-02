package com.demo.backend.application.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final JwtSecurityFilter jwtSecurityFilter;
  private final WebFilter corsFilter;

  public SecurityConfig(JwtSecurityFilter jwtSecurityFilter,
                        @Qualifier("customCorsFilter") WebFilter corsFilter) {
    this.jwtSecurityFilter = jwtSecurityFilter;
    this.corsFilter = corsFilter;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .addFilterAfter(corsFilter, SecurityWebFiltersOrder.CORS)
      .authorizeExchange(exchange -> exchange
        .pathMatchers("/swagger-ui/**").permitAll()
        .pathMatchers("/v3/api-docs/**").permitAll()
        .pathMatchers("/webjars/**").permitAll()
        .pathMatchers("/api-docs/**").permitAll()
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
        .pathMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
        .pathMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
        .anyExchange().authenticated()
      )
      .addFilterAt(jwtSecurityFilter, SecurityWebFiltersOrder.AUTHENTICATION)
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
