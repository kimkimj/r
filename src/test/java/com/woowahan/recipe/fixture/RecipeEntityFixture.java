package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;

public class RecipeEntityFixture {

    public static RecipeEntity get(UserEntity user) {
        return RecipeEntity.builder()
                .id(1L)
                .recipe_title("testTitle")
                .recipe_body("testBody")
                .user(user)
                .build();
    }
}
