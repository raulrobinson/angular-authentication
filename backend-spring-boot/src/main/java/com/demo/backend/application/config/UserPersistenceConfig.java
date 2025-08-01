package com.demo.backend.application.config;

import com.demo.backend.infrastructure.adapter.persistence.UserPersistenceAdapter;
import com.demo.backend.infrastructure.adapter.persistence.mapper.UserMapper;
import com.demo.backend.infrastructure.adapter.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class UserPersistenceConfig {

  @Bean
  public UserPersistenceAdapter userPersistenceAdapter(TransactionalOperator tx,
                                                       UserRepository userRepository,
                                                       UserMapper userMapper,
                                                       PasswordEncoder passwordEncoder) {
    return new UserPersistenceAdapter(tx, userRepository, userMapper, passwordEncoder);
  }
}
