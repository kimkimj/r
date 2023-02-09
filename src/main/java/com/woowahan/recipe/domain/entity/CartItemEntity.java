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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @Column(name = "order_check")
    private boolean isChecked;

    public static CartItemEntity createCartItem(Integer itemCnt, ItemEntity item, CartEntity cart) {
        return CartItemEntity.builder()
                .cartItemCnt(itemCnt)
                .item(item)
                .cart(cart)
                .isChecked(true)
                .build();
    }

    public void updateCartItemCnt(Integer itemCnt) {
        this.cartItemCnt = itemCnt;
    }

    public void updateCheckItem() {
        this.isChecked = !isChecked;
    }

}
