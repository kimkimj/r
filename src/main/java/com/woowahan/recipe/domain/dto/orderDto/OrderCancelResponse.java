package com.woowahan.recipe.domain.dto.orderDto;

import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCancelResponse {
    private Long id;
    private String message;
    private String orderNum;
    private OrderStatus orderStatus;

    @Builder
    public OrderCancelResponse(Long id, String orderNum, String message, OrderStatus orderStatus) {
        this.id = id;
        this.orderNum = orderNum;
        this.message = message;
        this.orderStatus = orderStatus;
    }

    public static OrderCancelResponse from(OrderEntity order) {
        return OrderCancelResponse.builder()
                .id(order.getId())
                .orderNum(order.getOrderNumber())
                .message("주문이 취소되었습니다.")
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
