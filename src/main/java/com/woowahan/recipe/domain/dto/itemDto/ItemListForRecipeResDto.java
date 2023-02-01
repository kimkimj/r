package com.woowahan.recipe.domain.dto.itemDto;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListForRecipeResDto {
    private Long id;
    private String name;
    private Integer itemPrice;
    private Integer itemStock;

    // TODO : 2023-01-31 레시피 등록시 재료 검색해서 나오는 페이지
    public static ItemListForRecipeResDto from(ItemEntity item) {
        return ItemListForRecipeResDto.builder()
                .id(item.getId())
                .name(item.getName())
                .itemPrice(item.getItemPrice())
                .itemStock(item.getItemStock())
                .build();
    }
}
