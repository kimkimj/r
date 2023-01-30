package com.woowahan.recipe.domain.dto.cartDto;

import com.woowahan.recipe.domain.entity.CartItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoResponse {

    private String itemName;

    private String itemImagePath;

    private Integer itemPrice;

    private Integer itemCnt;

    private Integer itemStock;

    public static CartInfoResponse from(CartItemEntity cartItemEntity) {
        return CartInfoResponse.builder()
                .itemName(cartItemEntity.getItem().getItemName())
                .itemImagePath(cartItemEntity.getItem().getItemImagePath())
                .itemPrice(cartItemEntity.getItem().getItemPrice())
                .itemCnt(cartItemEntity.getCartItemCnt())
                .itemStock(cartItemEntity.getItem().getItemStock())
                .build();
    }
}
