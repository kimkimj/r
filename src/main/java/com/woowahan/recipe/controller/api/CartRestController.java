package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import com.woowahan.recipe.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
