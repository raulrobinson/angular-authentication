package com.demo.backend.infrastructure.adapter.persistence;

import com.demo.backend.domain.api.UserAdapterPort;
import com.demo.backend.domain.model.User;
import com.demo.backend.infrastructure.adapter.persistence.entity.UserEntity;
import com.demo.backend.infrastructure.adapter.persistence.mapper.UserMapper;
import com.demo.backend.infrastructure.adapter.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserPersistenceAdapter implements UserAdapterPort {

  private final TransactionalOperator tx;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserPersistenceAdapter(TransactionalOperator tx, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
    this.tx = tx;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Mono<Boolean> existsByEmail(String name) {
    return userRepository.existsByEmail(name);
  }

  @Override
  public Mono<User> saveUser(User userRequest) {
    log.info("Saving user with email: {}", userRequest.getEmail());

    UserEntity user = userMapper.toEntityFromDomain(userRequest);
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

    return userRepository.save(user)
        .doOnError(error -> log.error("Error saving user: {}", error.getMessage()))
        .flatMap(savedUser -> tx.transactional(Mono.just(userMapper.toDomain(savedUser))));
  }

}
