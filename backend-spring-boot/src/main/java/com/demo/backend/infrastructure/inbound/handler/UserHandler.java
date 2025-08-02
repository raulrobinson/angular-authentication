package com.demo.backend.infrastructure.inbound.handler;

import com.demo.backend.application.config.security.JwtAdapter;
import com.demo.backend.domain.spi.UserServicePort;
import com.demo.backend.infrastructure.adapter.persistence.mapper.UserMapper;
import com.demo.backend.infrastructure.common.exception.GlobalErrorHandler;
import com.demo.backend.infrastructure.inbound.dto.LoginRequest;
import com.demo.backend.infrastructure.inbound.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;

import static com.demo.backend.domain.exception.TechnicalMessage.X_MESSAGE_ID;
import static com.demo.backend.infrastructure.common.Constants.CREATE_ERROR;
import static com.demo.backend.infrastructure.common.MessageHeaderHandler.getMessageId;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserServicePort service;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtAdapter jwtAdapter;
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

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(LoginRequest.class)
      .flatMap(login -> service.findUserByEmail(login.getEmail())
        .flatMap(user -> {
          if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
          }
          // Aquí generamos el token JWT
          return jwtAdapter.generateToken(user.getEmail(), List.of(user.getRole()))
            .flatMap(token -> service.saveToken(token, user.getEmail())
              .flatMap(success -> {
                if (success) {
                  return ServerResponse.ok().bodyValue(Map.of(
                    "token", token.getJwt(),
                    "messageId", getMessageId(request)
                  ));
                } else {
                  return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue(Map.of("error", "Failed to save token"));
                }
              }));
        }));
  }

  public Mono<ServerResponse> logout(ServerRequest request) {
    String token = request.headers().firstHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
    }
    String jwt = token.substring(7); // Remove "Bearer " prefix
    return service.existsTokenByJwt(jwt)
      .flatMap(existingToken -> service.logout(jwt)
        .flatMap(success -> {
          if (success) {
            return ServerResponse.ok().bodyValue(Map.of(
              "message", "Logout successful",
              "messageId", getMessageId(request)
            ));
          } else {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .bodyValue(Map.of("error", "Failed to logout"));
          }
        }))
      .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
      .doOnError(error -> log.error("❌ Error during logout: {}", error.getMessage()))
      .onErrorResume(exception -> globalErrorHandler.handle(exception, getMessageId(request)));
  }

}
