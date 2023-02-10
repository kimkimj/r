package com.woowahan.recipe.domain.dto.itemDto;

import com.woowahan.recipe.domain.entity.ItemEntity;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemListResDto {

    private Long id;
    private String itemImagePath;
    private String itemName;
    private Integer price;
    private Integer stock;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    public static ItemListResDto from(ItemEntity item) {
        return ItemListResDto.builder()
                .id(item.getId())
                .itemImagePath(item.getItemImagePath())
                .itemName(item.getName())
                .price(item.getItemPrice())
                .stock(item.getItemStock())
                .createdDate(item.getCreatedDate())
                .lastModified(item.getLastModifiedDate())
                .build();
    }

}
