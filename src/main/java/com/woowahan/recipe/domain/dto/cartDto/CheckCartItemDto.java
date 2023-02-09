package com.woowahan.recipe.domain.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckCartItemDto {

    private Long id;

    private boolean isChecked;

}
