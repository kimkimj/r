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

    private Long id;
    private String impUid;
    private String orderNum;
    private String itemName;
    private String username;
    private String address;
    private Integer totalPrice;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    @Builder
    public OrderInfoResponse(Long id, String impUid, String orderNum, String itemName, String username, String address, Integer totalPrice, OrderStatus orderStatus, DeliveryStatus deliveryStatus, LocalDateTime orderDate, LocalDateTime updateDate) {
        this.id = id;
        this.impUid = impUid;
        this.orderNum = orderNum;
        this.itemName = itemName;
        this.username = username;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.orderDate = orderDate;
        this.updateDate = updateDate;
    }

    public static OrderInfoResponse from(OrderEntity order) {
        return OrderInfoResponse.builder()
                .id(order.getId())
                .impUid(order.getImpUid())
                .orderNum(order.getOrderNumber())
                .itemName(order.getOrderItems().get(0).getItem().getName())
                .username(order.getUser().getName())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice() >= 50000 ? order.getTotalPrice() : order.getTotalPrice() + 3000)
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(order.getDelivery().getDeliveryStatus())
                .orderDate(order.getCreatedDate())
                .updateDate(order.getLastModifiedDate())
                .build();
    }
}
