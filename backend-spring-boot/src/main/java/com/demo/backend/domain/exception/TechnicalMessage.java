package com.demo.backend.domain.exception;

import lombok.Getter;

@Getter
public enum TechnicalMessage {
  BAD_REQUEST("400", "Bad request", ""),
  NOT_FOUND("404", "Not found", ""),
  ALREADY_EXISTS("409", "Already exists", ""),
  INTERNAL_SERVER_ERROR("500", "Internal server error", ""),
  DATABASE_ERROR("500", "Database error occurred", ""),
  X_MESSAGE_ID("X-Message-ID", "Unique identifier for the message", ""),
  UNAUTHORIZED("401", "Unauthorized access", "");

  private final String code;
  private final String message;
  private final String parameter;

  TechnicalMessage(String code, String message, String parameter) {
    this.code = code;
    this.message = message;
    this.parameter = parameter;
  }
}
