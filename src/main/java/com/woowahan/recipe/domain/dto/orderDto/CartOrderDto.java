package com.woowahan.recipe.domain.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDto {

    private Long id;  // 장바구니에 담긴 상품의 아이디 (!= 상품 자체 아이디)
    private Integer cnt;

}
