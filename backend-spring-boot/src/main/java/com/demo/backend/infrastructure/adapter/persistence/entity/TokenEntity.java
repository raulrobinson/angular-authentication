package com.demo.backend.infrastructure.adapter.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("tokens")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {

  @Id
  private UUID tokenId;      // puede ser UUID
  private UUID userId;
  private String jwt;
  private ZonedDateTime issuedAt;
  private ZonedDateTime expiresAt;
  private boolean active;      // marcarlo false en logout
}
