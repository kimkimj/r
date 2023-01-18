package com.woowahan.recipe.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class OrderEntity extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity delivery;

    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public OrderEntity(Long id, UserEntity user, DeliveryEntity delivery, OrderStatus orderStatus) {
        this.id = id;
        this.orderNumber = UUID.randomUUID().toString();
        this.user = user;
        this.delivery = delivery;
        this.orderStatus = orderStatus;
    }

    /* 연관관계 메서드 */
    public void addUser(UserEntity user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItemEntity orderItem) {
        orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

    public void addDelivery(DeliveryEntity delivery) {
        this.delivery = delivery;
        delivery.addOrder(this);
    }

    /* 생성 메서드 */
    public static OrderEntity createOrder(UserEntity user, DeliveryEntity delivery, OrderItemEntity... orderItems) {
        OrderEntity order = new OrderEntity();
        order.addUser(user);
        order.addDelivery(delivery);

        for (OrderItemEntity orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.orderStatus = OrderStatus.ORDER;
        return order;
    }

    /* 비지니스 로직 */
    public void cancel() {
        if (delivery.getDeliveryStatus().equals(DeliveryStatus.COMP)) {
            throw new RuntimeException("이미 배송이 완료되어 주문 취소가 불가능 합니다.");
        }

        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItemEntity orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItemEntity orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}
