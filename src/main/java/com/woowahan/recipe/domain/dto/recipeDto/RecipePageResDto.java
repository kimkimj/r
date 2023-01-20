package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipePageResDto {
    private Long recipe_id;
    private String recipe_title;
    private String userName;
    private int recipe_view;
    private int recipe_like;
//    private String thumbnail_image_path; 썸네일 추가시
}
