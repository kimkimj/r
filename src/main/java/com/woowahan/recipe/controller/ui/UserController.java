package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        return "redirect:/";
    }

}
