package com.woowahan.recipe.domain.dto.item;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemCreateReqDto {

    private String itemImagePath;
    private String itemName;
    private int itemPrice;
    private int itemStock;

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .itemImagePath(this.itemImagePath)
                .itemName(this.itemName)
                .itemPrice(this.itemPrice)
                .itemStock(this.itemStock)
                .build();
    }

}
