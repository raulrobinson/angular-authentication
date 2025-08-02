package com.demo.backend.application.config;

import com.demo.backend.domain.model.User;
import com.demo.backend.infrastructure.inbound.dto.LoginRequest;
import com.demo.backend.infrastructure.inbound.dto.UserRequest;
import com.demo.backend.infrastructure.inbound.handler.UserHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

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
    ),
    @RouterOperation(
      path = "/api/auth/login",
      produces = "application/json",
      method = org.springframework.web.bind.annotation.RequestMethod.POST,
      beanClass = UserHandler.class,
      beanMethod = "login",
      operation = @io.swagger.v3.oas.annotations.Operation(
        operationId = "login",
        summary = "User Login",
        description = "Logs in a user and returns authentication details.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Login Request DTO",
          content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequest.class)
          )
        ),
        responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "User logged in successfully",
          content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = User.class)
          )
        )
      )
    ),
    @org.springdoc.core.annotations.RouterOperation(
      path = "/api/auth/logout",
      produces = "application/json",
      method = RequestMethod.GET,
      beanClass = UserHandler.class,
      beanMethod = "logout",
      operation = @io.swagger.v3.oas.annotations.Operation(
        operationId = "logoutUser",
        summary = "User Logout",
        description = "Logout the user and invalidate the JWT token.",
        security = @SecurityRequirement(name = "bearerAuth")
      )
    )
  })
  public RouterFunction<ServerResponse> routes(UserHandler handler) {
    return RouterFunctions.route()
      .POST("/api/auth/register", accept(MediaType.APPLICATION_JSON), handler::createUser)
      .POST("/api/auth/login", accept(MediaType.APPLICATION_JSON), handler::login)
      .GET("/api/auth/logout", handler::logout)       // requiere JWT
      .build();
  }
}
