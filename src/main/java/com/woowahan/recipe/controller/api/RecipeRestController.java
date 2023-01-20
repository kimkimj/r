
package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeRestController {

    private final RecipeService recipeService;

    /**
     * TODO: 2023-01-17 레시피 단건 조회 api
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response<RecipeFindResDto> findRecipe(@PathVariable Long id) {
        recipeService.updateView(id);
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        return Response.success(recipeFindResDto);
    }

    /**
     * TODO: 2023-01-20 레시피 전체 조회 api
     * @param pageable
     * @return
     */
    @GetMapping
    public Response<Page<RecipePageResDto>> findAllRecipes(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(recipeService.findAllRecipes(pageable));
    }

    /**
     * TODO: 2023-01-18 레시피 작성 api
     * @param recipeCreateReqDto
     * @param authentication
     * @return
     */
    @PostMapping("")
    public Response<RecipeResponse> createRecipe(@RequestBody RecipeCreateReqDto recipeCreateReqDto, Authentication authentication) {
        String userName = authentication.getName();
        RecipeCreateResDto recipeCreateResDto = recipeService.createRecipe(recipeCreateReqDto, userName);
        return Response.success(new RecipeResponse("레시피를 등록했습니다.", recipeCreateResDto.getRecipe_id()));
    }

    /**
     * TODO: 2023-01-19 레시피 수정 api
     * @param recipeUpdateReqDto
     * @param id
     * @param authentication
     * @return
     */
    @PutMapping("/{id}")
    public Response<RecipeResponse> updateRecipe(@RequestBody RecipeUpdateReqDto recipeUpdateReqDto, @PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        RecipeUpdateResDto recipeUpdateResDto = recipeService.updateRecipe(recipeUpdateReqDto, id, userName);
        return Response.success(new RecipeResponse("레시피를 수정했습니다.", recipeUpdateResDto.getRecipe_id()));
    }

    /**
     * TODO : 2023-01-20 레시피 삭제 api
     * @param id
     * @param authentication
     * @return
     */
    @DeleteMapping("/{id}")
    public Response<RecipeResponse> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        RecipeResponse recipeResponse = recipeService.deleteRecipe(id, userName);
        return Response.success(recipeResponse);
    }
}