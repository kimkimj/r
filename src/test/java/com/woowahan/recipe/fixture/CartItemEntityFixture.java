package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.CartItemEntity;

public class CartItemEntityFixture {

    public static CartItemEntity get(String userName, String password) {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(2L)
                .cartItemCnt(10)
                .item(ItemEntityFixture.get())
                .cart(CartEntityFixture.get(userName, password))
                .build();
        return cartItem;
    }

}
