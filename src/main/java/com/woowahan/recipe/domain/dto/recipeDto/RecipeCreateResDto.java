package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeCreateResDto {
    private final String recipe_title;
    private final String recipe_body;
    private final String userName;
}
