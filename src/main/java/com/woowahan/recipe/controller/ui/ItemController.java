package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.itemDto.ItemDetailResDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemListResDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemSearchReqDto;
import com.woowahan.recipe.service.CartService;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    /**
     * paging -> 상품 전체 조회
     */
    private String paging(Model model, Page<ItemListResDto> items) {
        int nowPage = items.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, items.getTotalPages());
        int lastPage = items.getTotalPages();

        model.addAttribute("items", items);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "item/findAllForm";
    }

    /**
     * user - 상품 상세조회
     */
    @GetMapping("/{id}")
    public String findUserForm(Model model, @PathVariable Long id) {
        ItemDetailResDto resDto = itemService.findItem(id);

        model.addAttribute("id", id);
        model.addAttribute("resDto", resDto); // -> sellerName도 들어있음
        model.addAttribute("cartItemReq", new CartItemReq(id, 1));
        return "item/findForm";
    }

    /**
     * 카트에 상품 수량 업데이트 (카트에 아이템이 없으면 생성)
     */
    @PostMapping("/cart")
    public String addCartItem(@ModelAttribute CartItemReq cartItemReq,
                              Model model, Authentication authentication) {

        model.addAttribute("cartItemReq", cartItemReq);
        cartService.addCartItem(cartItemReq, authentication.getName());

        return "redirect:/items/"+cartItemReq.getCartItemId(); //상품상세보기페이지로 return
    }

    /**
     * 상품 전체조회
     */
    @GetMapping
    public String findAllForm(Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemListResDto> items = itemService.findAllItem(pageable);
        return paging(model, items);
    }

    /**
     * 상품 검색
     */
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute ItemSearchReqDto itemSearchReqDto, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemListResDto> searchItems = itemService.searchItem(itemSearchReqDto.getKeyword(), pageable);
        return paging(model, searchItems);
    }

}
