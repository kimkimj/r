package com.woowahan.recipe.domain.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckOrderItemDto {

    private Long id;

    private String isChecked;

}
