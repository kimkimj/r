package com.woowahan.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeCreateReqDto {
    private final String recipe_title;
    private final String recipe_body;
}
