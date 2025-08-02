package com.demo.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private UUID tokenId;      // puede ser UUID
    private UUID userId;
    private String jwt;
    private ZonedDateTime issuedAt;
    private ZonedDateTime expiresAt;
    private boolean active;      // marcarlo false en logout
}
