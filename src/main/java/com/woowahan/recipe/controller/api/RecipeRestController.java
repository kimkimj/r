
package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.service.CartService;
import com.woowahan.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recipes")
public class RecipeRestController {

    private final RecipeService recipeService;
    private final CartService cartService;

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
    @GetMapping("/list")
    public Response<Page<RecipePageResDto>> findAllRecipes(
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
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
                                                      @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
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
        return Response.success(new RecipeResponse("레시피를 등록했습니다.", recipeCreateResDto.getRecipeId()));
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
        return Response.success(new RecipeResponse("레시피를 수정했습니다.", recipeUpdateResDto.getRecipeId()));
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

    /**
     * @author 이소영
     * @param id
     * @date 2023-01-24
     * @return Response<Integer>
     * @description 좋아요 개수 세기 api
    **/
    @GetMapping("/{id}/likes")
    public Response<Integer> countLikes(@PathVariable Long id) {
        Integer likeCnt = recipeService.countLikes(id);
        return Response.success(likeCnt);
    }

    /**
     * @author 이다온
     * @param title
     * @param pageable
     * @date 2023-01-31
     * @return Response<Page<RecipePageResDto>>
     * @description 레시피 검색 api
     */
    @PostMapping("/search")
    public Response<Page<RecipePageResDto>> searchRecipes(@RequestBody String title, @PageableDefault(size = 50, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(recipeService.searchRecipes(title, pageable));
    }


    /**
     * 장바구니에 재료 담기
     */
    @PostMapping("/carts")
    public Response<String> addCartItemList(@RequestBody List<CartItemReq> cartItemReqList, Model model, Authentication authentication) throws IOException {
        cartService.addCartItemList(cartItemReqList, authentication.getName());
        return Response.success("장바구니에 상품이 담겼습니다. \n장바구니로 이동하시겠습니까?");
    }

}