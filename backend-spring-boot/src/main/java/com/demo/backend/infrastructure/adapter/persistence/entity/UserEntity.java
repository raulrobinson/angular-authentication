package com.demo.backend.infrastructure.adapter.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

  @Id
  private UUID id;
  private String name;
  private String email;
  private String password;
  private String role;
}
