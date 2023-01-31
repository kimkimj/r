package com.woowahan.recipe.domain.dto.itemDto;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemCreateReqDto {

    private String itemImagePath;
    @NotBlank(message = "상품명은 필수입니다.")
    private String itemName;
    @NotNull(message = "상품 가격은 필수입니다.")
    private Integer itemPrice;
    @NotNull(message = "상품 수량은 필수입니다.")
    private Integer itemStock;

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .itemImagePath(this.itemImagePath)
                .name(this.itemName)
                .itemPrice(this.itemPrice)
                .itemStock(this.itemStock)
                .build();
    }

}
