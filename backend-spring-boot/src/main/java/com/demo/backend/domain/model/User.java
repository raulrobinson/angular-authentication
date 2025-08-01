package com.demo.backend.domain.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  private UUID id;
  private String name;
  private String email;
  private String password;
}
