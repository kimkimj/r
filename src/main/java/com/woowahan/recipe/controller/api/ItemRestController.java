package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.item.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.item.ItemCreateResDto;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemRestController {

    private final ItemService itemService;

    /**
     * 재료 추가(관리자)
     */
    //관리자 권한 확인 추가 해줘야함
    @PostMapping
    public Response<ItemCreateResDto> createItem(@RequestBody ItemCreateReqDto itemCreateReqDto) {
        return Response.success(itemService.createItem(itemCreateReqDto));
    }




}
