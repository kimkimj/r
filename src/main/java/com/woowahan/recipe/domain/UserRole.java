package com.woowahan.recipe.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    HEAD("role_head"),
    ADMIN("role_admin"),
    USER("role_user");
    private String value;
}
