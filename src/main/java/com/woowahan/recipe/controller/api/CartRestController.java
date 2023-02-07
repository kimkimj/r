package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartOrderDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    public Response<Page<CartItemResponse>> findCartList(@PageableDefault(sort = "itemName", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page<CartItemResponse> cartInfoResponsePage = cartService.findCartItemList(pageable, userName);
        return Response.success(cartInfoResponsePage);
    }

    @PostMapping
    public Response<String> createCartItem (@RequestBody CartItemReq cartItemCreateReq, Authentication authentication) {
        String userName = authentication.getName();
        cartService.addCartItem(cartItemCreateReq, userName);
        return Response.success("상품이 장바구니에 담겼습니다.");
    }

    @PostMapping("/orders")
    public Response<OrderCreateResDto> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Authentication authentication) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        OrderCreateResDto orderCreateResDto = cartService.orderCartItem(cartOrderDtoList, authentication.getName());
        return Response.success(orderCreateResDto);
    }

    @PutMapping
    public Response<String> updateCartItem (@RequestBody CartItemReq cartItemUpdateReq, Authentication authentication) {
        String userName = authentication.getName();
        Integer cnt = cartService.updateCartItem(cartItemUpdateReq, userName);
        return Response.success(cnt + "개로 수량이 변경되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public Response<String> deleteCartItem(@PathVariable Long itemId, Authentication authentication) {
        String userName = authentication.getName();
        cartService.deleteCartItem(itemId, userName);
        return Response.success("장바구니에서 아이템에서 삭제되었습니다.");
    }
}
