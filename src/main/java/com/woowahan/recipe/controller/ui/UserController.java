package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserLoginReqDto;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("userJoinReqDto", new UserJoinReqDto());
        return "user/joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid UserJoinReqDto form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/joinForm";
        }
        model.addAttribute("userJoinReqDto", new UserJoinReqDto());
        userService.join(form);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("userLoginReqDto", new UserLoginReqDto());
        return "user/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserLoginReqDto userLoginReqDto, BindingResult result) {
        if (result.hasErrors()) {
            result.getFieldErrors().stream().forEach(err ->
                    log.info("field={} value={} msg={}", err.getField(), err.getRejectedValue(), err.getDefaultMessage()));
            return "user/loginForm";
        }

        userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
        return "redirect:/";
    }

    @GetMapping("/users/my/update")
    public String updateForm() {
        return "user/updateForm";
    }

    @PostMapping("/users/my/update")
    public String update() {
        return "user/updateForm";
    }

    @GetMapping("/users/my/recipe")
    public String myRecipe() {
        return "user/my/myRecipe";
    }

    @GetMapping("/users/my/orders")
    public String myOrders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Authentication authentication, Pageable pageable, Model model) {
        /*Page<OrderInfoResponse> orders = orderService.findAllOrder(authentication.getName(), pageable);
        model.addAttribute("orders", orders);*/
        return "user/my/myOrder";
    }

    @GetMapping("/users/my/get-reviews")
    public String myGetReviews() {
        return "user/my/myGetReview";
    }

    @GetMapping("/users/my/send-reviews")
    public String mySendReviews() {
        return "user/my/mySendReview";
    }

    @GetMapping("/users/my/recipe-like")
    public String myLikeRecipe() {
        return "user/my/myLikeRecipe";
    }

}
