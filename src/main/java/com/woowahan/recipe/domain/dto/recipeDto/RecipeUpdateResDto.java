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
    private Long recipeId;
    private String recipeTitle;
    private String recipeBody;
    private String userName;
    private LocalDateTime localDateTime;
}
