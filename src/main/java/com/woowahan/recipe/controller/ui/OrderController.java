package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.domain.entity.DeliveryEntity;
import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.entity.OrderItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    @GetMapping("{userId}/placeOrder/{orderId}")
    public String orderForm(@PathVariable Long userId, @PathVariable Long orderId, Model model) {
        String username = "GordonRamsey";
        OrderInfoResponse order = orderService.findOrder(username, orderId);
        model.addAttribute("orderInfoResponse", order);
        UserResponse userResponse = userService.findUser(userId);
        model.addAttribute("userResponse", userResponse);
        return "order/orderForm";
    }

}
