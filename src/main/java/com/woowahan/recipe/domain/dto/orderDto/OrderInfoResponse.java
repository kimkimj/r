package com.woowahan.recipe.domain.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.domain.entity.DeliveryStatus;
import com.woowahan.recipe.domain.entity.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderInfoResponse {

    private String orderNum;
    private String username;
    private String address;
    private Integer totalPrice;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;

    @Builder
    public OrderInfoResponse(String orderNum, String username, String address, Integer totalPrice, OrderStatus orderStatus, DeliveryStatus deliveryStatus, LocalDateTime orderDate) {
        this.orderNum = orderNum;
        this.username = username;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.orderDate = orderDate;
    }

    public static OrderInfoResponse from(OrderEntity order) {
        return OrderInfoResponse.builder()
                .orderNum(order.getOrderNumber())
                .username(order.getUser().getUserName())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(order.getDelivery().getDeliveryStatus())
                .orderDate(order.getCreatedDate())
                .build();
    }
}
