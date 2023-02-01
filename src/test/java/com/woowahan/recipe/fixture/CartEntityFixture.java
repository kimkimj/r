package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.UserEntity;

public class CartEntityFixture {

    public static CartEntity get(UserEntity user) {
        CartEntity cart = CartEntity.builder()
                .id(1L)
                .user(user)
                .build();
        return cart;
    }

}
