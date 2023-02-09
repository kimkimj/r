package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemListResDto;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.domain.dto.sellerDto.*;
import com.woowahan.recipe.domain.dto.userDto.UserLoginReqDto;
import com.woowahan.recipe.service.ItemService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final ItemService itemService;

    // 판매자 홈페이지
    @GetMapping("/sellerIndex")
    public String home() {
        return "seller/sellerIndex";
    }

    // 판매자 회원 가입
    @GetMapping("/seller/join")
    public String joinForm(Model model) {
        model.addAttribute("sellerJoinRequest", new SellerJoinRequest());
        return "seller/joinForm";
    }

    @PostMapping("/seller/join")
    public String join(Model model, @Valid SellerJoinRequest sellerJoinRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "seller/joinForm";
        }
        model.addAttribute("sellerJoinRequest", new SellerJoinRequest());
        sellerService.join(sellerJoinRequest);
        return "redirect:/seller/login";
    }

    // 로그인
    @GetMapping("/seller/login")
    public String loginForm(Model model) {
        model.addAttribute("sellerLoginRequest", new SellerLoginRequest());
        return "seller/loginForm";
    }

    @PostMapping("/seller/login")
    public String login(@Valid @ModelAttribute SellerLoginRequest sellerLoginRequest, BindingResult result,
                        HttpServletRequest httpServletRequest){
        if (result.hasErrors()) {
            result.getFieldErrors().stream().forEach(err ->
                    log.info("field={} value={} msg={}", err.getField(), err.getRejectedValue(), err.getDefaultMessage()));
            return "seller/loginForm";
        }

        // 세션 넣기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);

        String token = sellerService.login(sellerLoginRequest.getSellerName(), sellerLoginRequest.getPassword());
        session.setAttribute("jwt", "Bearer " + token);
        String checkJwt = (String) session.getAttribute("jwt");
        log.info("checkJwt={}", checkJwt);
        log.info("token={}", token);
        session.setMaxInactiveInterval(1800);

        return "redirect:/sellerIndex";
    }

    @GetMapping("/seller/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("jwt");
        session.invalidate();

        return "redirect:/";
    }

    // 마이 페이지
    @GetMapping("/seller/my")
    public String myPage(Model model, Authentication authentication) {
        SellerResponse seller = sellerService.findBySellerName(authentication.getName());
        model.addAttribute("seller", seller);
        return "seller/myInfo";
    }

    // 판매자 정보 수정 페이지
    @GetMapping("/seller/my/update")
    public String update(Model model, Authentication authentication) {
        String sellerName = authentication.getName();
        SellerResponse seller = sellerService.findBySellerName(sellerName);
        model.addAttribute("seller", seller);
        return "seller/updateForm";
    }

    @PostMapping("/seller/my/update")
    public String update(Model model, Authentication authentication, SellerUpdateRequest request) {
        String sellerName = authentication.getName();
        SellerResponse seller = sellerService.update(sellerName, request);
        model.addAttribute("seller", seller);
        return "redirect:/seller/my";
    }

    // 비밀번호 수정 페이지
    @GetMapping("/seller/my/password")
    public String passwordForm(Model model) {
        model.addAttribute("sellerPasswordUpdateRequest", new SellerPasswordUpdateRequest());
        return "seller/passwordUpdate";
    }

    @PostMapping("/seller/my/password")
    public String updatePassword(Model model, Authentication authentication,
                                 @ModelAttribute SellerPasswordUpdateRequest request) {
        model.addAttribute("sellerPasswordUpdateRequest", request);
       sellerService.updatePassword(authentication.getName(), request);
       return "redirect:/seller/my";
    }

    // 내 상품 리스트
    @GetMapping("/seller/my/items")
    public String myItems(Model model, Authentication authentication,
                            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = authentication.getName();
        Page<ItemListResDto> itemList = itemService.findAllBySeller(authentication.getName(), pageable);

        int nowPage = itemList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, itemList.getTotalPages());
        int lastPage = itemList.getTotalPages();

        model.addAttribute("myItemList", itemList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "seller/myItems";
    }
}
