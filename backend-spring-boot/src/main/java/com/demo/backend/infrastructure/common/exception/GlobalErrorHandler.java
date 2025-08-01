package com.demo.backend.infrastructure.common.exception;

import com.demo.backend.domain.exception.DuplicateResourceException;
import com.demo.backend.domain.exception.TechnicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@Order(-2)
public class GlobalErrorHandler {

  public Mono<ServerResponse> handle(Throwable throwable, String messageId) {
    log.error("Exception captured globally: {}", throwable.toString());

    return switch (throwable) {

      case UnauthorizedException ex -> buildErrorResponse(
        HttpStatus.UNAUTHORIZED,
        messageId,
        TechnicalMessage.UNAUTHORIZED,
        List.of(ErrorDTO.of(
          ex.getMessage(),
          TechnicalMessage.UNAUTHORIZED.getParameter()
        ))
      );

      case DuplicateResourceException ex -> buildErrorResponse(
        HttpStatus.CONFLICT,
        messageId,
        TechnicalMessage.ALREADY_EXISTS,
        List.of(ErrorDTO.of(
          ex.getMessage(),
          ex.getParameter()
        ))
      );

      default -> buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        messageId,
        TechnicalMessage.INTERNAL_SERVER_ERROR,
        List.of(ErrorDTO.of(
          throwable.getMessage(),
          TechnicalMessage.INTERNAL_SERVER_ERROR.getParameter()
        ))
      );
    };
  }

  private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus,
                                                  String messageId,
                                                  TechnicalMessage technicalMessage,
                                                  List<ErrorDTO> errors) {
    return ServerResponse.status(httpStatus)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(APIResponse.builder()
        .code(httpStatus.value())
        .message(technicalMessage.getMessage())
        .timestamp(Instant.now().toString())
        .identifier(messageId)
        .errors(errors)
        .build());
  }
}
