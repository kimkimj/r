package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.entity.DeliveryEntity;
import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.entity.OrderItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class OrderController {


    @GetMapping("/placeOrder")
    public String orderForm(Model model) {
        model.addAttribute("deliveryEntity", new DeliveryEntity());
        model.addAttribute("userEntity", new UserEntity());
        model.addAttribute("orderEntity", new OrderEntity());
        model.addAttribute("orderItemEntity", new OrderItemEntity());
        return "order/orderForm";
    }



}
