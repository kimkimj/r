package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.LikeEntity;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;

public class LikeEntityFixture {

    public static LikeEntity get(UserEntity user, RecipeEntity recipe) {
        LikeEntity likeEntity = LikeEntity.builder()
                .id(1L)
                .user(user)
                .recipe(recipe)
                .build();
        return likeEntity;
    }
}
