package com.demo.backend.infrastructure.adapter.persistence.repository;

import com.demo.backend.domain.model.Token;
import com.demo.backend.infrastructure.adapter.persistence.entity.TokenEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface TokenRepository extends ReactiveCrudRepository<TokenEntity, UUID> {

  @Query("""
          DELETE FROM tokens
          WHERE user_id = :userId
          AND active = true
    """)
  Mono<Void> deactivateActiveTokensByUserId(UUID userId);

  @Query("""
      INSERT INTO tokens (user_id, jwt, active, expires_at)
      VALUES (:userId, :token.token, true, :token.expiresAt)
      ON CONFLICT (user_id) DO UPDATE
      SET jwt = EXCLUDED.jwt,
          active = true,
          expires_at = EXCLUDED.expires_at
      RETURNING *
  """)
  Mono<TokenEntity> saveToken(UUID userId, Token token);

  Mono<Void> deleteTokenEntitiesByExpiresAt(ZonedDateTime expiresAt);

  @Modifying
  @Query("DELETE FROM tokens WHERE expires_at < now()")
  Mono<Integer> deleteExpiredTokens();

  @Modifying
  @Query("UPDATE tokens SET active = false WHERE expires_at < now() AND active = true")
  Mono<Integer> deactivateExpiredTokens();

  Mono<Boolean> existsByJwt(String jwt);

  @Modifying
  @Query("UPDATE tokens SET active = false WHERE jwt = :jwt AND active = true")
  Mono<Integer> deactivateTokenByJwt(@Param("jwt") String jwt);
}
