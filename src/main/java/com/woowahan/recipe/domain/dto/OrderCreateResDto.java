package com.woowahan.recipe.domain.dto;

import com.woowahan.recipe.domain.entity.OrderEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateResDto {

    private String orderNumber;
    private String receiveUserName;
    private String phoneNum;
    private String address;

    @Builder
    public OrderCreateResDto(String orderNumber, String receiveUserName, String phoneNum, String address) {
        this.orderNumber = orderNumber;
        this.receiveUserName = receiveUserName;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public static OrderCreateResDto from(OrderEntity order) {
        return OrderCreateResDto.builder()
                .orderNumber(order.getOrderNumber())
                .receiveUserName(order.getUser().getUserName())
                .phoneNum(order.getUser().getPhoneNum())
                .address(order.getUser().getAddress())
                .build();
    }
}
