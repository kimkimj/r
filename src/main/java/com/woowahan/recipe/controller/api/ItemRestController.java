package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemRestController {

    private final ItemService itemService;

    /**
     * 재료 전체 조회
     */
    @GetMapping
    public Response<Page<ItemListResDto>> findAllItem(@PageableDefault(size = 50, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(itemService.findAllItem(pageable));
    }


    /**
     * 재료 단건(상세) 조회
     */
    @GetMapping("/{id}")
    public Response<ItemDetailResDto> findItem(@PathVariable Long id) {
        return Response.success(itemService.findItem(id));
    }

    /**
     * 재료 검색
     */
    @PostMapping("/search")
    public Response<Page<ItemListResDto>> searchItem(@RequestBody ItemSearchReqDto itemSearchReqDto, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(itemService.searchItem(itemSearchReqDto.getKeyword(), pageable));
    }

    // 특정 판매자가 등록한 재료  전체 조회
    @GetMapping("/items/{sellerName}")
    public Response<Page<ItemListResDto>> findAllBySeller(@PathVariable String sellerName, Pageable pageable) {
        return Response.success(itemService.findAllBySeller(sellerName, pageable));
    }

    /**
     * 재료 등록(관리자)
     */
    @PostMapping
    public Response<ItemCreateResDto> createItem(@RequestBody ItemCreateReqDto itemCreateReqDto, Authentication authentication) {
        return Response.success(itemService.createItem(itemCreateReqDto, authentication.getName()));
    }

    /**
     * 재료 수정(관리자)
     */
    @PutMapping("/{id}")
    public Response<ItemUpdateResDto> updateItem(@PathVariable Long id, @RequestBody ItemUpdateReqDto itemUpdateReqDto,
                                                 Authentication authentication) {
        return Response.success(itemService.updateItem(id, itemUpdateReqDto, authentication.getName()));
    }

    /**
     * 재료 삭제(관리자)
     */
    @DeleteMapping("/{id}")
    public Response<ItemDeleteResDto> deleteItem(@PathVariable Long id, Authentication authentication) {
        return Response.success(itemService.deleteItem(id, authentication.getName()));
    }
}
