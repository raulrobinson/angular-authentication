package com.demo.backend.infrastructure.adapter.persistence.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;

    Roles(String roleName) {
        this.roleName = roleName;
    }

}
