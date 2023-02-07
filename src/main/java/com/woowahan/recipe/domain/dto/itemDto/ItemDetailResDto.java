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

    private Long itemId;
    private String itemImagePath;
    private String itemName;
    private Integer itemPrice;
    private Integer itemStock;
    private String sellerName;
    private String message;

    public static ItemDetailResDto from(ItemEntity item) {
        return ItemDetailResDto.builder()
                .itemId(item.getId())
                .itemImagePath(item.getItemImagePath())
                .itemName(item.getName())
                .itemPrice(item.getItemPrice())
                .sellerName(item.getSeller().getSellerName())
                .itemStock(item.getItemStock())
                .build();
    }

}
