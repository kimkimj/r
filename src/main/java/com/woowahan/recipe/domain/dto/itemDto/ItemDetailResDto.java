package com.woowahan.recipe.domain.dto.itemDto;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ItemDetailResDto {

    private String itemImagePath;
    private String itemName;
    private Integer itemPrice;
    private Integer itemStock;
    private String message;

    public static ItemDetailResDto from(ItemEntity item) {
        return ItemDetailResDto.builder()
                .itemImagePath(item.getItemImagePath())
                .itemName(item.getName())
                .itemPrice(item.getItemPrice())
                .itemStock(item.getItemStock())
                .build();
    }

}
