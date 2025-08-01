package com.demo.backend.infrastructure.adapter.persistence.repository;

import com.demo.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
  Mono<Boolean> existsByEmail(String email);
}
