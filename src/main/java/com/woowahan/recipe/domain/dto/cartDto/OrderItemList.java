package com.woowahan.recipe.domain.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemList {

    private List<CheckOrderItemDto> orderItems;

}
