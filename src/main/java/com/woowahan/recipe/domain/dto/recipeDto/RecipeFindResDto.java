package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
