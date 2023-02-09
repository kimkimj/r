package com.woowahan.recipe.domain.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowahan.recipe.domain.entity.DeliveryStatus;
import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreateResDto {

    private Long id;
    private String orderNumber;
    private String receiveUserName;
    private String phoneNum;
    private String address;
    private int totalPrice;
    private int totalCount;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;

    @Builder
    public OrderCreateResDto(Long id, String orderNumber, String receiveUserName, String phoneNum, String address, int totalPrice, int totalCount, OrderStatus orderStatus, DeliveryStatus deliveryStatus, LocalDateTime orderDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.receiveUserName = receiveUserName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.orderDate = orderDate;
    }

    public static OrderCreateResDto from(OrderEntity order) {
        return OrderCreateResDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .receiveUserName(order.getUser().getUserName())
                .phoneNum(order.getUser().getPhoneNum())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice())
                .totalCount(order.getTotalCounts())
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(order.getDelivery().getDeliveryStatus())
                .orderDate(order.getCreatedDate())
                .build();
    }
}
