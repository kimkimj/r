package com.woowahan.recipe.domain.dto.recipeDto;

import com.woowahan.recipe.domain.entity.RecipeItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCreateReqDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String recipeTitle;
    @NotBlank(message = "내용을 입력해주세요.")
    private String recipeBody;
    private List<RecipeItemEntity> recipes;
}
