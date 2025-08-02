package com.demo.backend.domain.usecase;

import com.demo.backend.domain.api.UserAdapterPort;
import com.demo.backend.domain.exception.BusinessException;
import com.demo.backend.domain.exception.DuplicateResourceException;
import com.demo.backend.domain.exception.InvalidValueException;
import com.demo.backend.domain.exception.TechnicalMessage;
import com.demo.backend.domain.model.Token;
import com.demo.backend.domain.model.User;
import com.demo.backend.domain.spi.UserServicePort;
import org.springframework.data.crossstore.ChangeSetPersister;
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

  @Override
  public Mono<User> findUserByEmail(String email) {
    if (email == null || email.isEmpty()) {
      return Mono.error(new IllegalArgumentException("Email must not be null or empty"));
    }

    return userAdapter.findUserByEmail(email)
      .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
  }

  @Override
  public Mono<Boolean> saveToken(Token token, String email) {
    if (token.getJwt() == null || email == null) {
      return Mono.error(new InvalidValueException(
        "Email and Token",
        "must not be null"
      ));
    }

    return userAdapter.saveToken(token, email)
      .onErrorResume(e -> Mono.error(new BusinessException(
        TechnicalMessage.BAD_REQUEST.getMessage(), "Error saving token: ", e.getMessage())));
  }

  @Override
  public Mono<Boolean> existsTokenByJwt(String jwt) {
    if (jwt == null || jwt.isEmpty()) {
      return Mono.error(new InvalidValueException("JWT", "must not be null or empty"));
    }

    return userAdapter.existsTokenByJwt(jwt)
      .onErrorResume(e -> Mono.error(new BusinessException(
        TechnicalMessage.BAD_REQUEST.getMessage(), "Error checking token existence: ", e.getMessage())));
  }

  @Override
  public Mono<Boolean> logout(String jwt) {
    if (jwt == null || jwt.isEmpty()) {
      return Mono.error(new InvalidValueException("JWT", "must not be null or empty"));
    }

    return userAdapter.logout(jwt)
      .onErrorResume(e -> Mono.error(new BusinessException(
        TechnicalMessage.BAD_REQUEST.getMessage(), "Error during logout: ", e.getMessage())));
  }
}
