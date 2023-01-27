package com.woowahan.recipe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    private Integer cartItemCnt;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;


    public static CartItemEntity createCartItem(Integer itemCnt, ItemEntity item, CartEntity cart) {
        return CartItemEntity.builder()
                .cartItemCnt(itemCnt)
                .item(item)
                .cart(cart)
                .build();
    }

}
