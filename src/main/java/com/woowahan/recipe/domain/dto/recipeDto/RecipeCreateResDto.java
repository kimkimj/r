package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class RecipeCreateResDto {
    private Long recipe_id;
    private String recipe_title;
    private String recipe_body;
    private String userName;
    private LocalDateTime createdAt;
}
