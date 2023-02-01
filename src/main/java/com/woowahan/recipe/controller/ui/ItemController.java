package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;


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
     * 상품 상세조회
     */
    @GetMapping("/{id}")
    public String findForm(Model model, @PathVariable Long id) {
        ItemDetailResDto resDto = itemService.findItem(id);
        model.addAttribute("id", id);
        model.addAttribute("resDto", resDto);
        return "item/findForm";
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
        model.addAttribute("items", searchItems);
        return "item/findAllForm";
    }


    /**
     * 재료 등록(관리자, 판매자)
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("itemCreateReqDto", new ItemCreateReqDto());
        return "item/createForm";
    }

    @PostMapping("/create")
    //동작 test용
//    public String create(@Valid ItemCreateReqDto reqDto, BindingResult bindingResult, Model model, Authentication authentication) {
    public String create(@Valid @ModelAttribute ItemCreateReqDto reqDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "item/createForm";
        }
        ItemCreateResDto resDto = itemService.createItem(reqDto, "ididid4");

        redirectAttributes.addAttribute("id", resDto.getId());
        return "redirect:/items/{id}";
    }

    /**
     * 재료 수정(관리자, 판매자)
     */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id,  Model model) {
        model.addAttribute("id", id);
        model.addAttribute("itemUpdateReqDto", itemService.findItem(id));
        return "item/updateForm";
    }

    @PostMapping("/update/{id}")
    //동작 test용
//    public String update(@PathVariable Long id, @Valid @ModelAttribute ItemUpdateReqDto reqDto, BindingResult bindingResult, Model model, Authentication authentication) {
    public String update(@Valid @ModelAttribute ItemUpdateReqDto reqDto, BindingResult bindingResult,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "item/updateForm";
        }
        ItemUpdateResDto resDto = itemService.updateItem(id, reqDto, "ididid4");
        redirectAttributes.addAttribute("id", resDto.getId());
        return "redirect:/items/{id}";
    }

    /**
     * 재료 삭제(관리자, 판매자)
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        itemService.deleteItem(id, "ididid");
        return "redirect:/items";
    }

}
