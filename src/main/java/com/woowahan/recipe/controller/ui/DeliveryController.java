package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.domain.entity.DeliveryEntity;
import com.woowahan.recipe.domain.entity.DeliveryStatus;
import com.woowahan.recipe.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final OrderService orderService;

    @GetMapping("delivery/{deliveryId}")
    public String getDeliveryStatus(Model model, @PathVariable Long deliveryId) {
        String username = "GordonRamsey";
        orderService.findOrder(username, deliveryId);
        model.addAttribute("deliveryEntity", new DeliveryEntity());
        model.addAttribute("deliveryStatus", DeliveryStatus.values());
        return "order/delivery";
    }
}
