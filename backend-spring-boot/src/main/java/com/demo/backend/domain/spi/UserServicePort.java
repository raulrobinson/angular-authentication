package com.demo.backend.domain.spi;

import com.demo.backend.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserServicePort {

  Mono<User> saveUser(User userRequest);
}
