package com.demo.backend.application.config.beans;

import com.demo.backend.domain.api.UserAdapterPort;
import com.demo.backend.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

  @Bean
  public UserUseCase userUseCase(UserAdapterPort userAdapter) {
    return new UserUseCase(userAdapter);
  }
}
