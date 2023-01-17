package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/recipes/")
public class RecipeRestController {

    private final RecipeService recipeService;

    @GetMapping("/{id}")
    public Response<RecipeFindResDto> findRecipe(@PathVariable Long id){
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        return Response.success(recipeFindResDto);
    }
}
