
package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.domain.dto.Response;
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
    public Response<RecipeResponse> createRecipe(@RequestBody RecipeCreateReqDto recipeCreateReqDto, Authentication authentication) {
        String userName = authentication.getName();
        RecipeCreateResDto recipeCreateResDto = recipeService.createRecipe(recipeCreateReqDto, userName);
        return Response.success(new RecipeResponse("레시피를 등록했습니다.", recipeCreateResDto.getRecipe_id()));
    }

    @PutMapping("/{id}")
    public Response<RecipeResponse> updateRecipe(@RequestBody RecipeUpdateReqDto recipeUpdateReqDto, @PathVariable Long id,Authentication authentication) {
        String userName = authentication.getName();
        RecipeUpdateResDto recipeUpdateResDto = recipeService.updateRecipe(recipeUpdateReqDto, id, userName);
        return Response.success(new RecipeResponse("레시피를 수정했습니다.", recipeUpdateResDto.getRecipe_id()));
    }
}