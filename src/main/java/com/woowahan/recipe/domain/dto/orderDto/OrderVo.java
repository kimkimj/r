package com.woowahan.recipe.domain.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

    private String impUid;
    private Long itemId;
    private Integer cost;

}
