package com.woowahan.recipe.domain.dto.orderDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateReqDto {

    private Long itemId;
    private int count;

    @Builder
    public OrderCreateReqDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }
}
