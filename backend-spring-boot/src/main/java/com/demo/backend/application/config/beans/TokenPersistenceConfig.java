package com.demo.backend.application.config.beans;

import com.demo.backend.infrastructure.adapter.persistence.TokenPersistenceAdapter;
import com.demo.backend.infrastructure.adapter.persistence.repository.TokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenPersistenceConfig {

  @Bean
  public TokenPersistenceAdapter tokenPersistenceAdapter(TokenRepository tokenRepository) {
    return new TokenPersistenceAdapter(tokenRepository);
  }
}
