package com.woowahan.recipe.domain.dto.itemDto;

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
public class ItemUpdateReqDto {

    private String itemImagePath;
    @NotBlank(message = "상품명은 필수입니다.")
    private String itemName;
    @NotNull(message = "상품 가격은 필수입니다.")
    private Integer itemPrice;
    @NotNull(message = "상품 수량은 필수입니다.")
    private Integer itemStock;

}
