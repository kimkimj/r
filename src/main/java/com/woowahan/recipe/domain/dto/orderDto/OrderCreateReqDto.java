package com.woowahan.recipe.domain.dto.orderDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateReqDto {

    private String imp_uid;
    private Long itemId;
    private int count;

    private int deliveryCost;
    private int totalCost;

    @Builder
    public OrderCreateReqDto(String imp_uid, Long itemId, int count, int deliveryCost, int totalCost) {
        this.imp_uid = imp_uid;
        this.itemId = itemId;
        this.count = count;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
    }
}
