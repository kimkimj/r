package com.woowahan.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    HEAD("role_head"),
    ADMIN("role_admin"),
    SELLER("role_seller"),
    USER("role_user");
    private String value;
}
