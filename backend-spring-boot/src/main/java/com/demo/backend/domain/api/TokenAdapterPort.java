package com.demo.backend.domain.api;

import reactor.core.publisher.Mono;

public interface TokenAdapterPort {
  Mono<Boolean> deactivateExpiredTokens();
}
