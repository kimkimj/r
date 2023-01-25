package com.woowahan.recipe.domain.dto.orderDto;

import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.OrderStatus;
import lombok.Builder;

public class OrderInfoResponse {

    private String orderNum;
    private String username;
    private String address;
    private Integer totalPrice;
    private OrderStatus orderStatus;

    @Builder
    public OrderInfoResponse(String orderNum, String username, String address, Integer totalPrice, OrderStatus orderStatus) {
        this.orderNum = orderNum;
        this.username = username;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public static OrderInfoResponse from(OrderEntity order) {
        return OrderInfoResponse.builder()
                .orderNum(order.getOrderNumber())
                .username(order.getUser().getUserName())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
