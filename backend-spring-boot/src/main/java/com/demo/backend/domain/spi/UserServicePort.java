package com.demo.backend.domain.spi;

import com.demo.backend.domain.model.Token;
import com.demo.backend.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserServicePort {

  Mono<User> saveUser(User userRequest);

  Mono<User> findUserByEmail(String email);
  Mono<Boolean> saveToken(Token token, String email);

  Mono<Boolean> existsTokenByJwt(String jwt);

  Mono<Boolean> logout(String jwt);
}
