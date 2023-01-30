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
public class ItemUpdateResDto {

    private Long id;
    private String itemImagePath;
    private String itemName;
    private Integer itemPrice;
    private Integer itemStock;
    private String message;

    public static ItemUpdateResDto from(ItemEntity item) {
        return ItemUpdateResDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemStock(item.getItemStock())
                .message("상품 수정 완료")
                .build();
    }


}
