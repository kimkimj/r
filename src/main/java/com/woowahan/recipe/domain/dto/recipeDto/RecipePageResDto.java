package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipePageResDto {
    private Long recipeId;
    private String recipeTitle;
    private String userName;
    private int recipeView;
    private int recipeLike;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
//    private String thumbnailImagePath; 썸네일 추가시
}
