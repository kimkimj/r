package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeFindResDto {
    private Long recipeId;
    private String recipeTitle;
    private String recipeBody;
    private String userName;
    private int recipeLike;
    private int recipeView;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
