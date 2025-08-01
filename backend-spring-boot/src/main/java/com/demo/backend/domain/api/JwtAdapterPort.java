package com.demo.backend.domain.api;

import com.demo.backend.domain.model.Token;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JwtAdapterPort {
    Mono<Token> generateToken(String email, List<String> roles);
    Mono<Boolean> validateJwt(String jwt);
}
