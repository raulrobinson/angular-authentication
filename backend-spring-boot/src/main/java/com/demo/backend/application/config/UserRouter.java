package com.demo.backend.application.config;

import com.demo.backend.domain.model.User;
import com.demo.backend.infrastructure.inbound.dto.UserRequest;
import com.demo.backend.infrastructure.inbound.handler.UserHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

  @Bean
  @RouterOperations({
    @RouterOperation(
      path = "/api/auth/register",
      produces = "application/json",
      method = org.springframework.web.bind.annotation.RequestMethod.POST,
      beanClass = UserHandler.class,
      beanMethod = "createUser",
      operation = @io.swagger.v3.oas.annotations.Operation(
        operationId = "createUser",
        summary = "Create User",
        description = "Creates a new user.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "User Request DTO",
          content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserRequest.class)
          )
        ),
        responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201",
          description = "User created successfully",
          content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = User.class)
          )
        )
      )
    )
  })
  public RouterFunction<ServerResponse> routes(UserHandler handler) {
    return RouterFunctions.route()
      .POST("/api/auth/register", handler::createUser)
      .build();
  }
}
