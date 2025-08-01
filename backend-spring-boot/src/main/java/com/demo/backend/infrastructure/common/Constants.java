package com.demo.backend.infrastructure.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String CREATE_ERROR = "Error creating resource: {} and messageId: {}";
    public static final String LIST_ERROR = "Error listing resources: {}";
    public static final String DELETE_ERROR = "Error deleting capability: {}";
    public static final String FIND_ERROR = "Error finding capability: {}";
}
