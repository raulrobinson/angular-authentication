package com.demo.backend.domain.api;

import com.demo.backend.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserAdapterPort {

  Mono<Boolean> existsByEmail(String name);

  Mono<User> saveUser(User userRequest);
}
