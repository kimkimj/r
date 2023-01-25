package com.woowahan.recipe.domain.dto.orderDto;

import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDeleteResDto {

    private String orderNum;
    private String message;
    private OrderStatus orderStatus;

    @Builder
    public OrderDeleteResDto(String orderNum, String message, OrderStatus orderStatus) {
        this.orderNum = orderNum;
        this.message = message;
        this.orderStatus = orderStatus;
    }

    public static OrderDeleteResDto from(OrderEntity order) {
        return OrderDeleteResDto.builder()
                .orderNum(order.getOrderNumber())
                .message("주문이 취소되었습니다.")
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
