package com.demo.backend.infrastructure.adapter.persistence.mapper;

import com.demo.backend.domain.model.User;
import com.demo.backend.infrastructure.adapter.persistence.entity.UserEntity;
import com.demo.backend.infrastructure.inbound.dto.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Named("toDomain")
  default User toDomain(UserEntity entity) {
    if (entity == null) return null;
    return User.builder()
      .id(entity.getId())
      .name(entity.getName())
      .email(entity.getEmail())
      .password(entity.getPassword())
      .build();
  }

  @Named("toEntityFromRequest")
  default UserEntity toEntityFromRequest(UserRequest userRequest) {
    if (userRequest == null) return null;
    return UserEntity.builder()
      .name(userRequest.getName())
      .email(userRequest.getEmail())
      .password(userRequest.getPassword())
      .build();
  }

  @Named("toDomainFromRequest")
  default User toDomainFromRequest(UserRequest userRequest) {
    if (userRequest == null) return null;
    return User.builder()
      .name(userRequest.getName())
      .email(userRequest.getEmail())
      .password(userRequest.getPassword())
      .build();
  }

  @Named("toEntityFromDomain")
  default UserEntity toEntityFromDomain(User userRequest) {
    if (userRequest == null) return null;
    return UserEntity.builder()
      .name(userRequest.getName())
      .email(userRequest.getEmail())
      .password(userRequest.getPassword())
      .build();
  }

}
