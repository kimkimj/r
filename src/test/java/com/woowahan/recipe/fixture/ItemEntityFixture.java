package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.ItemEntity;

public class ItemEntityFixture {

    public static ItemEntity get() {
        ItemEntity item = ItemEntity.builder()
                                    .id(1L)
                                    .itemName("testItemName")
                                    .itemPrice(1000)
                                    .itemStock(5)
                                    .build();
        return item;
    }

}
