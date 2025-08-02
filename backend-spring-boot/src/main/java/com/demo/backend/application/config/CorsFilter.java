package com.demo.backend.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;

@Configuration
public class CorsFilter {

  @Bean(name = "customCorsFilter")
  public WebFilter corsFilter() {
    return (exchange, chain) -> {
      ServerHttpResponse response = exchange.getResponse();
      response.getHeaders().add("Access-Control-Allow-Origin", "*");
      response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
      if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
        response.setStatusCode(HttpStatus.OK);
        return response.setComplete();
      }
      return chain.filter(exchange);
    };
  }
}
