package com.woowahan.recipe.domain.dto.cartDto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReq {

    private Long itemId;

    private Integer cartItemCnt;

}
