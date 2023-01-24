package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        return UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password(password)
                .name("testName")
                .birth("2023-xx-xx")
                .address("testAddress")
                .email("testEmail")
                .phoneNum("010-1234-5678")
                .userRole(UserRole.USER)
                .cartEntity(null)
                .orders(null)
                .build();
    }
}
