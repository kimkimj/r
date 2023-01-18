package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RecipeCreateResDto {
    private String recipe_title;
    private String recipe_body;
    private String userName;
}
