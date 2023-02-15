package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.exception.AppException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.woowahan.recipe.domain.OrderStatus.CANCEL;
import static com.woowahan.recipe.domain.OrderStatus.ORDER;
import static com.woowahan.recipe.exception.ErrorCode.ALREADY_ARRIVED;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity delivery;

    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Setter
    private OrderStatus orderStatus;

    private String impUid;

    @Builder
    public OrderEntity(Long id, UserEntity user, DeliveryEntity delivery, OrderStatus orderStatus, String imp_uid) {
        this.id = id;
        this.orderNumber = createOrderNumber();
        this.user = user;
        this.delivery = delivery;
        this.orderStatus = orderStatus;
        this.impUid = imp_uid;
    }

    /* 연관관계 메서드 */
    public void addUser(UserEntity user) {
        this.user = user;
    }

    public void addOrderItem(OrderItemEntity orderItem) {
        orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

    public void addDelivery(DeliveryEntity delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /* 생성 메서드 */
    // 개별 주문
    public static OrderEntity createOrder(UserEntity user, DeliveryEntity delivery, OrderItemEntity orderItem, String imp_uid) {
        OrderEntity order = new OrderEntity();
        order.addUser(user);
        order.addDelivery(delivery);
        order.addOrderItem(orderItem);
        order.orderNumber = order.createOrderNumber();
        order.setOrderStatus(ORDER);
        order.impUid = imp_uid;
        return order;
    }

    // 장바구니 주문
    public static OrderEntity createOrder(UserEntity user, DeliveryEntity delivery, List<OrderItemEntity> orderItems, String imp_uid) {
        OrderEntity order = new OrderEntity();
        order.addUser(user);
        order.addDelivery(delivery);

        for (OrderItemEntity orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.orderNumber = order.createOrderNumber();
        order.setOrderStatus(ORDER);
        order.impUid = imp_uid;
        return order;
    }
    /* 비지니스 로직 */

    public String createOrderNumber() {
        String date = String.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));
        String now = date.replace("-", "");
        String substring = UUID.randomUUID().toString().substring(0, 8);
        return now + substring;
    }

    public void cancel() {
        if (delivery.getDeliveryStatus().equals(DeliveryStatus.COMP)) {
            throw new AppException(ALREADY_ARRIVED, ALREADY_ARRIVED.getMessage());
        }

        this.orderStatus = CANCEL;
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

    public int getTotalCounts() {
        int totalCount = 0;
        for (OrderItemEntity orderItem : orderItems) {
            totalCount += orderItem.getCount();
        }
        return totalCount;
    }

}
