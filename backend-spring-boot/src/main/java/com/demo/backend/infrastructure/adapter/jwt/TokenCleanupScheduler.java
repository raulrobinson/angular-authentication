package com.demo.backend.infrastructure.adapter.jwt;

import com.demo.backend.domain.api.TokenAdapterPort;
import com.demo.backend.infrastructure.adapter.persistence.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

  private final TokenAdapterPort tokenAdapterPort;

  // Ejecutar cada hora (puedes ajustar esto)
  @Scheduled(cron = "0 0 * * * *") // cada hora en punto
  //@Scheduled(cron = "*/30 * * * * *") // cada 30 segundos
  public void cleanExpiredTokens() {
    log.info("⏳ Starting scheduled cleanup of expired JWT tokens...");
    tokenAdapterPort.deactivateExpiredTokens()
      .doOnError(e -> log.error("❌ Error cleaning expired tokens: {}", e.getMessage()))
      .subscribe();
  }

}
