package com.woowahan.recipe.domain.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDto {

    private String imp_uid;
    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;
    private int totalCost;
}
