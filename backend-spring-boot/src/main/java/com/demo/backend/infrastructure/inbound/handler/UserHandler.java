package com.demo.backend.infrastructure.inbound.handler;

import com.demo.backend.domain.spi.UserServicePort;
import com.demo.backend.infrastructure.adapter.persistence.mapper.UserMapper;
import com.demo.backend.infrastructure.common.exception.GlobalErrorHandler;
import com.demo.backend.infrastructure.inbound.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static com.demo.backend.domain.exception.TechnicalMessage.X_MESSAGE_ID;
import static com.demo.backend.infrastructure.common.Constants.CREATE_ERROR;
import static com.demo.backend.infrastructure.common.MessageHeaderHandler.getMessageId;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserServicePort service;
  private final UserMapper userMapper;
  private final GlobalErrorHandler globalErrorHandler;

  public Mono<ServerResponse> createUser(ServerRequest request) {
    var messageId = getMessageId(request);
    return request.bodyToMono(UserRequest.class)
      .flatMap(user -> service.saveUser(userMapper.toDomainFromRequest(user)))
      .flatMap(createdUser -> ServerResponse.ok().bodyValue(createdUser))
      .contextWrite(Context.of(X_MESSAGE_ID, getMessageId(request)))
      .doOnError(error -> log.error(CREATE_ERROR, error.getMessage(), messageId))
      .onErrorResume(exception -> globalErrorHandler.handle(exception, getMessageId(request)));
  }

}
