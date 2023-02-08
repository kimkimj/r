package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.CartEntity;

public class CartEntityFixture {

    public static CartEntity get(String userName, String password) {
        CartEntity cart = CartEntity.builder()
                .id(1L)
                .user(UserEntityFixture.get(userName, password))
                .build();
        return cart;
    }

}
