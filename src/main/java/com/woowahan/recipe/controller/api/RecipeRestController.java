
package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeRestController {

    private final RecipeService recipeService;

    @GetMapping("/{id}")
    public Response<RecipeFindResDto> findRecipe(@PathVariable Long id){
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        return Response.success(recipeFindResDto);
    }

    @PostMapping("")
    public Response<RecipeCreateResDto> createRecipe(@RequestBody RecipeCreateReqDto recipeCreateReqDto, Authentication authentication) {
        String userName = authentication.getName();
        RecipeCreateResDto recipeCreateResDto = recipeService.createRecipe(recipeCreateReqDto, userName);
        return Response.success(recipeCreateResDto);
    }
}