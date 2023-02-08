package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartOrderList;
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

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final FindService findService;

    @PostMapping("/order")  // 장바구니에서 주문하기를 누를 때 주문 결제 페이지로 이동
    public String orderForm(Model model,@RequestBody CartOrderList cartOrderListDto) {
        log.info("cartOrderDtoList={}", cartOrderListDto.getCartOrderList().toString());
        for (CartOrderDto dto :cartOrderListDto.getCartOrderList()) {
            log.info("id = {}", dto.getId());
            log.info("name = {}", dto.getName());
            log.info("cnt = {}", dto.getCnt());
        }
        log.info("imp={}", cartOrderListDto.getImp_uid());
        log.info("cost={}", cartOrderListDto.getTotalCost());
//        UserResponse userResponse = findService.findUserName(authentication.getName());
//        model.addAttribute("userResponse", userResponse);
        model.addAttribute("cartOrderList", cartOrderListDto.getCartOrderList());
        model.addAttribute("totalCost", cartOrderListDto.getTotalCost());
        model.addAttribute("authentication", cartOrderListDto.getTotalCost());
        return "redirect:/carts/orderForm";
    }

    @GetMapping("/orderForm")
    public String moveOrderForm(Model model, CartOrderList cartOrderListDto, Authentication authentication) {
        log.info("cartOrderDtoList={}", cartOrderListDto.getCartOrderList().toString());
        for (CartOrderDto dto :cartOrderListDto.getCartOrderList()) {
            log.info("id = {}", dto.getId());
            log.info("name = {}", dto.getName());
            log.info("cnt = {}", dto.getCnt());
        }
        log.info("imp={}", cartOrderListDto.getImp_uid());
        log.info("cost={}", cartOrderListDto.getTotalCost());

        UserResponse userResponse = findService.findUserName(authentication.getName());
        model.addAttribute("userResponse", userResponse);
        model.addAttribute("cartOrderList", cartOrderListDto.getCartOrderList());
        return "cart/orderForm";
    }

    @GetMapping
    public String cartItemList(Model model, @PageableDefault(sort = "itemName", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        log.debug("cartItemList() 실행");
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

}
