
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
     * @author 김응준
     * @param id
     * @date 2023-01-17
     * @return Response<RecipeFindResDto>
     * @description 레시피 단건 조회 api
    **/
    @GetMapping("/{id}")
    public Response<RecipeFindResDto> findRecipe(@PathVariable Long id) {
        recipeService.updateView(id);
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        return Response.success(recipeFindResDto);
    }

    /**
     * @author 김응준
     * @param pageable
     * @date 2023-01-20
     * @return Response<Page<RecipePageResDto>>
     * @description 레시피 전체 조회 api
    **/
    @GetMapping
    public Response<Page<RecipePageResDto>> findAllRecipes(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(recipeService.findAllRecipes(pageable));
    }

    /**
     * @author 김응준
     * @param authentication
     * @param pageable
     * @date 2023-01-20
     * @return Response<Page<RecipePageResDto>>
     * @description 레시피 마이피드 api
    **/
    @GetMapping("/my")
    public Response<Page<RecipePageResDto>> myRecipes(Authentication authentication,
                                                      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = authentication.getName();
        return Response.success(recipeService.myRecipes(pageable, userName));
    }

    /**
     * @author 김응준
     * @param recipeCreateReqDto
     * @param authentication
     * @date 2023-01-18
     * @return Response<RecipeResponse>
     * @description 레시피 작성 api
    **/
    @PostMapping("")
    public Response<RecipeResponse> createRecipe(@RequestBody RecipeCreateReqDto recipeCreateReqDto, Authentication authentication) {
        String userName = authentication.getName();
        RecipeCreateResDto recipeCreateResDto = recipeService.createRecipe(recipeCreateReqDto, userName);
        return Response.success(new RecipeResponse("레시피를 등록했습니다.", recipeCreateResDto.getRecipe_id()));
    }

    /**
     * @author 김응준
     * @param recipeUpdateReqDto
     * @param id
     * @param authentication
     * @date 2023-01-19
     * @return Response<RecipeResponse>
     * @description 레시피 수정 api
    **/
    @PutMapping("/{id}")
    public Response<RecipeResponse> updateRecipe(@RequestBody RecipeUpdateReqDto recipeUpdateReqDto, @PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        RecipeUpdateResDto recipeUpdateResDto = recipeService.updateRecipe(recipeUpdateReqDto, id, userName);
        return Response.success(new RecipeResponse("레시피를 수정했습니다.", recipeUpdateResDto.getRecipe_id()));
    }

    /**
     * @author 김응준
     * @param id
     * @param authentication
     * @date 2023-01-20
     * @return Response<RecipeResponse>
     * @description 레시피 삭제 api
    **/
    @DeleteMapping("/{id}")
    public Response<RecipeResponse> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        RecipeResponse recipeResponse = recipeService.deleteRecipe(id, userName);
        return Response.success(recipeResponse);
    }

    /**
     * @author 이소영
     * @param authentication
     * @date 2023-01-24
     * @return Response<String>
     * @description 좋아요 누르기 api
    **/
    @PostMapping("/{id}/likes")
    public Response<String> pushLikes(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        String pushLikesMessage = recipeService.pushLikes(id, userName);
        return Response.success(pushLikesMessage);
    }

    @GetMapping("/{id}/likes")
    public Response<Integer> countLikes(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        Integer likeCnt = recipeService.countLikes(id, userName);
        return Response.success(likeCnt);
    }
}