package com.woowahan.recipe.domain.dto.recipeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeUpdateReqDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String recipeTitle;
    @NotBlank(message = "내용을 입력해주세요.")
    private String recipeBody;
}
