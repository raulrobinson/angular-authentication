package com.demo.backend.infrastructure.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    description = "User Request DTO",
    title = "UserRequest"
)
public class UserRequest {

  @Schema(description = "User name", example = "John Doe")
  private String name;

  @Schema(description = "User email", example = "john.doe@email.com")
  private String email;

  @Schema(description = "User password", example = "password123")
  private String password;
}
