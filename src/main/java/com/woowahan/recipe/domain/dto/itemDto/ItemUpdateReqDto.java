package com.woowahan.recipe.domain.dto.itemDto;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemUpdateReqDto {

    private String itemImagePath;
    private String itemName;
    private Integer itemPrice;
    private Integer itemStock;

}
