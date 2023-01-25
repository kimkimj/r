package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password(password)
                .name("testName")
                .address("서울특별시")
                .email("test@gmail.com")
                .phoneNum("010-1234-5678")
                .userRole(UserRole.USER)
                .birth("2000-01-01")
                .build();
        return userEntity;
    }

    public static UserEntity getFromUser (String userName, String password) {
        UserEntity userEntity = UserEntity.builder()
                .id(2L)
                .userName(userName)
                .password(password)
                .name("testName")
                .address("대전광역시")
                .email("testtest@gmail.com")
                .phoneNum("010-1111-2222")
                .userRole(UserRole.USER)
                .birth("2000-12-31")
                .build();
        return userEntity;
    }
}
