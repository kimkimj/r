package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.sellerDto.SellerListResponse;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.service.SellerService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final UserService userService;
    private final SellerService sellerService;

    @GetMapping
    public String moveUpdateView(){
        return "admin/navPage";
    }

    // 회원 관리 페이지
    @GetMapping("/users")
    public String updateUserPage(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable){
        Page<UserResponse> userList = userService.findAll(pageable);
        int nowPage = userList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, userList.getTotalPages());
        int lastPage = userList.getTotalPages();
        log.info("userNowPage = {}, userStartPage = {}, userEndPage = {}, userLastPage = {}", nowPage, startPage, endPage, lastPage);

        model.addAttribute("userList", userList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/updateUser";
    }

    // 판매자 관리 페이지
    @GetMapping("/sellers")
    public String updateSellerPage(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<SellerListResponse> sellerList = sellerService.findAll(pageable);
        int nowPage = sellerList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, sellerList.getTotalPages());
        int lastPage = sellerList.getTotalPages();
        log.info("sellerNowPage = {}, sellerStartPage = {}, sellerEndPage = {}, sellerLastPage = {}", nowPage, startPage, endPage, lastPage);

        model.addAttribute("sellerList", sellerList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/updateSeller";
    }
}
