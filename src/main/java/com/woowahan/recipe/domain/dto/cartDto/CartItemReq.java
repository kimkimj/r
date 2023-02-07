package com.woowahan.recipe.domain.dto.cartDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReq {

    private Long cartItemId;

    private Integer cartItemCnt;

}
