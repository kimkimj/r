package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeFindResDto {
    private Long recipe_id;
    private String recipe_title;
    private String recipe_body;
    private String userName;
    private Long recipe_like;
    private Long recipe_view;
}
