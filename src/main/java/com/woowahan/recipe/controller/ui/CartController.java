package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CartController {

    @GetMapping("/carts")
    public String cartForm(Model model) {
        model.addAttribute("CartInfo", new CartInfoResponse());
        return "cart/cartForm";
    }
}
