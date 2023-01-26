package com.woowahan.recipe.domain.dto.orderDto.search;

import com.woowahan.recipe.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch{
    private String itemName;
    private OrderStatus orderStatus;
}
