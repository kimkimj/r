package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    @GetMapping
    public String cartForm(Model model, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("cartInfoResponse", new CartInfoResponse());
        return "cart/cartForm";
    }
}
