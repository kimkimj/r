package com.woowahan.recipe.domain.dto.cartDto;

import com.woowahan.recipe.domain.dto.orderDto.CartOrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderList {

    private String imp_uid;
    private List<CartOrderDto> getCartOrderList;
    private int totalCost;

}
