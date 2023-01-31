package com.woowahan.recipe.domain.dto.cartDto;

import com.woowahan.recipe.domain.entity.CartItemEntity;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long id;

    private String name;

    private String imagePath;

    private Integer price;

    private Integer cnt;

    private Integer stock;

    public static CartItemResponse from(CartItemEntity cartItemEntity) {
        return CartItemResponse.builder()
                .id(cartItemEntity.getItem().getId())
                .name(cartItemEntity.getItem().getName())
                .imagePath(cartItemEntity.getItem().getItemImagePath())
                .price(cartItemEntity.getItem().getItemPrice())
                .cnt(cartItemEntity.getCartItemCnt())
                .stock(cartItemEntity.getItem().getItemStock())
                .build();
    }
}
