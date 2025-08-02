package com.demo.backend.infrastructure.adapter.persistence;

import com.demo.backend.domain.api.UserAdapterPort;
import com.demo.backend.domain.model.Token;
import com.demo.backend.domain.model.User;
import com.demo.backend.infrastructure.adapter.persistence.entity.TokenEntity;
import com.demo.backend.infrastructure.adapter.persistence.entity.UserEntity;
import com.demo.backend.infrastructure.adapter.persistence.enums.Roles;
import com.demo.backend.infrastructure.adapter.persistence.mapper.UserMapper;
import com.demo.backend.infrastructure.adapter.persistence.repository.TokenRepository;
import com.demo.backend.infrastructure.adapter.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
public class UserPersistenceAdapter implements UserAdapterPort {

  @Value("${jwt.expiration-time-sec:30}")
  private int tokenExpirationSeconds;

  private final TransactionalOperator tx;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserPersistenceAdapter(TransactionalOperator tx, UserRepository userRepository, TokenRepository tokenRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
    this.tx = tx;
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
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
    user.setRole(Roles.USER.getRoleName());

    return userRepository.save(user)
        .doOnError(error -> log.error("Error saving user: {}", error.getMessage()))
        .flatMap(savedUser -> tx.transactional(Mono.just(userMapper.toDomain(savedUser))));
  }

  @Override
  public Mono<User> findUserByEmail(String email) {
    log.info("Finding user by email: {}", email);
    return userRepository.findByEmail(email)
        .doOnError(error -> log.error("Error finding user by email: {}", error.getMessage()))
        .map(userMapper::toDomain);
  }

  @Override
  public Mono<Boolean> saveToken(Token token, String email) {
    return findUserByEmail(email)
      .switchIfEmpty(Mono.defer(() -> {
        log.error("❌ User not found for email: {}", email);
        return Mono.empty();
      }))
      .flatMap(user -> {
        log.info("✅ User found for email: {}", email);
        return deactivateActiveTokensByUserId(user.getId().toString())
          .then(tokenRepository.save(TokenEntity.builder()
            .jwt(token.getJwt())
            .active(true)
            .expiresAt(ZonedDateTime.now().plusSeconds(tokenExpirationSeconds)) // Establecer expiración válida
            .userId(user.getId()) // UUID directo
            .build()))
          .doOnSuccess(savedToken -> log.info("✅ Token saved for user: {}", user.getEmail()))
          .doOnError(error -> log.error("❌ Error saving token: {}", error.getMessage()));
      })
      .thenReturn(true)
      .defaultIfEmpty(false);
  }

  @Override
  public Mono<Boolean> existsTokenByJwt(String jwt) {
    log.info("Checking if token exists for JWT: {}", jwt);
    return tokenRepository.existsByJwt(jwt)
        .doOnError(error -> log.error("Error checking token existence: {}", error.getMessage()));
  }

  @Override
  public Mono<Boolean> logout(String jwt) {
    log.info("🔒 Logging out user with JWT: {}", jwt);

    return tokenRepository.deactivateTokenByJwt(jwt)
      .doOnSuccess(updated -> {
        if (updated > 0) {
          log.info("✅ Token deactivated successfully for JWT: {}", jwt);
        } else {
          log.warn("⚠️ No active token found for JWT: {}", jwt);
        }
      })
      .doOnError(error -> log.error("❌ Error deactivating token: {}", error.getMessage()))
      .map(updated -> updated > 0); // Devuelve true si se desactivó, false si no
  }

  private Mono<Void> deactivateActiveTokensByUserId(String userId) {
    log.info("Deactivating active tokens for userId: {}", userId);
    return tokenRepository.deactivateActiveTokensByUserId(UUID.fromString(userId))
        .doOnError(error -> log.error("Error deactivating tokens for userId {}: {}", userId, error.getMessage()))
        .then();
  }

}
