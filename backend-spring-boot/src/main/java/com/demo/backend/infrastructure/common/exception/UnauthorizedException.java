package com.demo.backend.infrastructure.common.exception;

import com.demo.backend.domain.exception.TechnicalMessage;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
