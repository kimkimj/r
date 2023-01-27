package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartItemCreateReq;
import com.woowahan.recipe.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    public Response<Page<CartInfoResponse>> findCartList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page<CartInfoResponse> cartInfoResponsePage = cartService.findCartItemList(pageable, userName);
        return Response.success(cartInfoResponsePage);
    }

    @PostMapping
    public Response<String> createCartItem (@RequestBody CartItemCreateReq cartItemCreateReq, Authentication authentication) {
        String userName = authentication.getName();
        cartService.createCartItem(cartItemCreateReq, userName);
        return Response.success("상품이 장바구니에 담겼습니다.");
    }

    @PutMapping("/{itemId}")
    public Response<String> updateCartItem (@PathVariable Long itemId, Integer itemCnt, Authentication authentication) {
        String userName = authentication.getName();
        cartService.updateCartItem(itemId, itemCnt, userName);
        return Response.success("수량이 변경되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public Response<String> deleteCartItem(@PathVariable Long itemId, Authentication authentication) {
        String userName = authentication.getName();
        cartService.deleteCartItem(itemId, userName);
        return Response.success("장바구니에서 아이템에서 삭제되었습니다.");
    }
}
