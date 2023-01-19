package com.woowahan.recipe.domain.dto.itemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ItemDeleteResDto {

    private Long id;
    private String message;

    public static ItemDeleteResDto from(Long id) {
        return ItemDeleteResDto.builder().
                id(id).
                message("재료 삭제 완료").
                build();
    }
}
