package com.woowahan.recipe.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private OrderEntity order;

    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public DeliveryEntity(String address, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
