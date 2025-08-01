package com.demo.backend.domain.usecase;

import com.demo.backend.domain.api.UserAdapterPort;
import com.demo.backend.domain.exception.DuplicateResourceException;
import com.demo.backend.domain.exception.TechnicalMessage;
import com.demo.backend.domain.model.User;
import com.demo.backend.domain.spi.UserServicePort;
import reactor.core.publisher.Mono;

public class UserUseCase implements UserServicePort {

  private final UserAdapterPort userAdapter;

  public UserUseCase(UserAdapterPort userAdapter) {
    this.userAdapter = userAdapter;
  }

  @Override
  public Mono<User> saveUser(User userRequest) {
    return userAdapter.existsByEmail(userRequest.getEmail())
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(new DuplicateResourceException(TechnicalMessage.ALREADY_EXISTS, "409", userRequest.getEmail()));
          }
          return userAdapter.saveUser(userRequest);
        });
  }
}
