package com.woowahan.recipe.domain.dto.orderDto;

import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateResDto {

    private String orderNumber;
    private String receiveUserName;
    private String phoneNum;
    private String address;
    private int totalPrice;
    private int totalCount;
    private OrderStatus orderStatus;

    @Builder
    public OrderCreateResDto(String orderNumber, String receiveUserName, String phoneNum, String address, int totalPrice, int totalCount, OrderStatus orderStatus) {
        this.orderNumber = orderNumber;
        this.receiveUserName = receiveUserName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.orderStatus = orderStatus;
    }

    public static OrderCreateResDto from(OrderEntity order) {
        return OrderCreateResDto.builder()
                .orderNumber(order.getOrderNumber())
                .receiveUserName(order.getUser().getUserName())
                .phoneNum(order.getUser().getPhoneNum())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice())
                .totalCount(order.getTotalCounts())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
