package com.woowahan.recipe.domain.dto.reviewDto;

// String만 받지만 나중에 혹시나 레시피에 대한 별점을 주는 기능을 추가 할 수 도 있어서만들었다

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewRequest {
    private String comment;

    public ReviewEntity toEntity(UserEntity user, RecipeEntity recipe, String content) {
        return ReviewEntity.builder()
                .user(user)
                .review_comment(content)
                .recipe(recipe)
                .build();
    }
}
