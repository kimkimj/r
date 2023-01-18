package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateResDto;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemRestController {

    private final ItemService itemService;

    /**
     * 재료 등록(관리자)
     */
    @PostMapping
    public Response<ItemCreateResDto> createItem(@RequestBody ItemCreateReqDto itemCreateReqDto, Authentication authentication) {
        return Response.success(itemService.createItem(itemCreateReqDto, authentication.getName()));
    }







}
