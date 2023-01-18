package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.item.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.item.ItemCreateResDto;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 재료 추가(관리자)
     */
    public ItemCreateResDto createItem(ItemCreateReqDto itemCreateReqDto) {
        ItemEntity item = itemCreateReqDto.toEntity();
        ItemEntity savedItem = itemRepository.save(item);
        return ItemCreateResDto.from(savedItem);
    }

}
