package com.demo.backend.infrastructure.adapter.persistence;

import com.demo.backend.domain.api.TokenAdapterPort;
import com.demo.backend.infrastructure.adapter.persistence.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class TokenPersistenceAdapter implements TokenAdapterPort {

  private final TokenRepository tokenRepository;

  public TokenPersistenceAdapter(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  public Mono<Boolean> deactivateExpiredTokens() {
    log.info("🔄 Deactivating expired tokens based on 'expires_at' < now()");

    return tokenRepository.deactivateExpiredTokens()
      .doOnSuccess(count -> log.info("✅ {} tokens marked as inactive", count))
      .doOnError(error -> log.error("❌ Error marking expired tokens as inactive", error))
      .thenReturn(true);

//    log.info("Deactivating all expired JWT tokens based on 'expires_at' field");
//
//    return tokenRepository.deleteExpiredTokens()
//      .doOnSuccess(count -> log.info("✅ Deactivated {} expired tokens", count))
//      .doOnError(error -> log.error("❌ Error deactivating expired tokens", error))
//      .thenReturn(true);
  }
}
