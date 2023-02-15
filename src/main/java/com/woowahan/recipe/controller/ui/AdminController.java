package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.adminDto.AdminResponse;
import com.woowahan.recipe.domain.dto.sellerDto.SellerListResponse;
import com.woowahan.recipe.service.AdminService;
import com.woowahan.recipe.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final SellerService sellerService;

    @GetMapping("/admin")
    public String moveUpdateView(){
        return "admin/navPage";
    }

    // logout
    @GetMapping("/admin/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName("token");
        cookieGenerator.addCookie(response, "deleted");
        cookieGenerator.setCookieMaxAge(0);

        return "redirect:/";
    }

    @GetMapping("/head/logout")
    public String headLogout(HttpSession session, HttpServletResponse response) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName("token");
        cookieGenerator.addCookie(response, "deleted");
        cookieGenerator.setCookieMaxAge(0);

        return "redirect:/";
    }

    // ADMIN - 회원 관리 페이지
    @GetMapping("/admin/users")
    public String updateAdminPage(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication){
        String userName = authentication.getName();
        Page<AdminResponse> userList = adminService.findAll(pageable, userName);
        int nowPage = userList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, userList.getTotalPages());
        int lastPage = userList.getTotalPages();

        model.addAttribute("userList", userList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/updateUser";
    }

    // HEAD - 회원 관리 페이지
    @GetMapping("/head/users")
    public String updateHeadPage(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication){
        String userName = authentication.getName();
        Page<AdminResponse> userList = adminService.findAll(pageable, userName);
        int nowPage = userList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, userList.getTotalPages());
        int lastPage = userList.getTotalPages();

        model.addAttribute("userList", userList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/updateUser";
    }

    // 판매자 관리 페이지
    @GetMapping("/admin/sellers")
    public String updateSellerPage(Model model, @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<SellerListResponse> sellerList = sellerService.findAll(pageable);
        int nowPage = sellerList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, sellerList.getTotalPages());
        int lastPage = sellerList.getTotalPages();

        model.addAttribute("sellerList", sellerList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/updateSeller";
    }
}
