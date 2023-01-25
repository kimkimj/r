package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    /**
     * 재료 등록(관리자, 판매자)
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("itemCreateReqDto", new ItemCreateReqDto());
        return "item/createForm";
    }

    @PostMapping("/create")
    public String create(@Validated ItemCreateReqDto reqDto, BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "item/createForm";
        }
        model.addAttribute("itemCreateReqDto", new ItemCreateReqDto());
        itemService.createItem(reqDto, authentication.getName());
        return "redirect:/";
    }

    /**
     * 재료 수정(관리자, 판매자)
     */
    @GetMapping("{id}/update")
    public String updateForm(Model model) {
        model.addAttribute("itemUpdateReqDto", new ItemUpdateReqDto());
        return "item/updateForm";
    }

    @PostMapping("{id}/update")
    public String update(@PathVariable Long id, @Validated ItemUpdateReqDto reqDto, BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "item/updateForm";
        }
        model.addAttribute("itemUpdateReqDto", new ItemUpdateReqDto());
        itemService.updateItem(id, reqDto, authentication.getName());
        return "redirect:/";
    }

}
