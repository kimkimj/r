package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartOrderListDto;
import com.woowahan.recipe.domain.dto.orderDto.CartOrderDto;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.service.CartService;
import com.woowahan.recipe.service.FindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final FindService findService;

    @GetMapping("/order")
    public String orderForm(Model model, @ModelAttribute CartOrderListDto orderDto, Authentication authentication) {
        UserResponse userResponse = findService.findUserName(authentication.getName());
        CartOrderListDto cartOrderListDto = cartService.findCartItemOrder(authentication.getName(), orderDto.getImp_uid());
        List<CartOrderDto> cartOrderDtoList = cartOrderListDto.getCartOrderList();

        String firstItem = cartOrderDtoList.get(0).getName();

        model.addAttribute("userResponse", userResponse);
        model.addAttribute("cartOrderDtoList", cartOrderDtoList);
        model.addAttribute("cartOrderListDto", cartOrderListDto);
        model.addAttribute("firstItem", firstItem);
        return "cart/orderForm";
    }


    @GetMapping
    public String cartItemList(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
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
        cartService.deleteCartItem(itemId, authentication.getName());
        return "redirect:/carts";
    }

}
