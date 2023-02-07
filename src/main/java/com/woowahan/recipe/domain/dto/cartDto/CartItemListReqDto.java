package com.woowahan.recipe.domain.dto.cartDto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemListReqDto {
    private Long id;
    private final Integer cartItemCnt = 1;
    private List<String> items;
}
