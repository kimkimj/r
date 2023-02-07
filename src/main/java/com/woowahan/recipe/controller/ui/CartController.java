package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String cartItemList(Model model, @PageableDefault(sort = "itemName", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        log.debug("cartItemList() 실행");
        String userName = "user";
        Page<CartItemResponse> cartList = cartService.findCartItemList(pageable, authentication.getName());

        // pagination
        int nowPage = cartList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, cartList.getTotalPages());
        int lastPage = cartList.getTotalPages();

        model.addAttribute("cartList", cartList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "cart/cartList";
    }

    @PostMapping("/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, Authentication authentication) {
        log.info("itemId : {}", itemId);
        cartService.deleteCartItem(itemId, authentication.getName());
        return "redirect:/carts";
    }

    @PatchMapping("/update/{itemId}")
    public ResponseEntity<Integer> updateCartItem(@PathVariable Long itemId, CartItemReq cartItemReq, @PageableDefault(sort = "itemName", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        log.debug("updateCartItem() 실행");
        Integer cnt = cartService.updateCartItem(cartItemReq, authentication.getName());
        return ResponseEntity.ok(cnt);
    }
}
