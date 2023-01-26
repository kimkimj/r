package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.domain.entity.DeliveryEntity;
import com.woowahan.recipe.domain.entity.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    @GetMapping("{id}")
    public String updateForm(Model model) {
        model.addAttribute("deliveryEntity", new DeliveryEntity());
        //model.addAttribute("deliveryStatus", new DeliveryStatus();
        return "order/deliveryForm";
    }
}
