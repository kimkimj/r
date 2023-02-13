package com.woowahan.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    HEAD("ROLE_HEAD"),
    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_SELLER"),
    READY("ROLE_READY"),
    REJECT("ROLE_REJECT"),
    USER("ROLE_USER");

    private String value;
}
