package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeFindResDto {
    private Long recipe_id;
    private String recipe_title;
    private String recipe_body;
    private String user_nickname;
    private Long recipe_like;
    private Long recipe_view;
}
