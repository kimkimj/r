package com.woowahan.recipe.domain.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemCreateReq {
    private Long itemId;

    private Integer itemCnt;
}
