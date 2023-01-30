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
public class ItemListResDto {

//  재료 id가 response에 꼭 필요할까 ?
    private Long id;
    private String itemImagePath;
    private String itemName;
    private Integer price;
    private Integer stock;

    public static ItemListResDto from(ItemEntity item) {
        return ItemListResDto.builder()
                .id(item.getId())
                .itemImagePath(item.getItemImagePath())
                .itemName(item.getName())
                .price(item.getItemPrice())
                .stock(item.getItemStock())
                .build();
    }

}
