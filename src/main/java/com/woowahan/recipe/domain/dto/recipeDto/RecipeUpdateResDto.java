package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RecipeUpdateResDto {
    private Long recipe_id;
    private String recipe_title;
    private String recipe_body;
    private String userName;
    private LocalDateTime localDateTime;
}
