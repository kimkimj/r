package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserLoginReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    // 관리자 페이지
    @GetMapping("/admin/users")
    public String admin(Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserResponse> userList = userService.findAll(pageable);
        model.addAttribute("userList", userList);
        return "user/admin";
    }

    // 회원가입
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

    // 로그인
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("userLoginReqDto", new UserLoginReqDto());
        return "user/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserLoginReqDto userLoginReqDto, BindingResult result,
                        HttpServletRequest httpServletRequest){
        if (result.hasErrors()) {
            result.getFieldErrors().stream().forEach(err ->
                    log.info("field={} value={} msg={}", err.getField(), err.getRejectedValue(), err.getDefaultMessage()));
            return "user/loginForm";
        }

        // 세션 넣기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);

        String token = userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
        session.setAttribute("jwt", "Bearer " + token);
        String checkJwt = (String) session.getAttribute("jwt");
        log.info("checkJwt={}", checkJwt);
        log.info("token={}", token);
        session.setMaxInactiveInterval(1800);

        return "redirect:/";
    }

    @GetMapping("/users/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("jwt");
        session.invalidate();
        return "redirect:/";
    }

    // 회원 정보 수정
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/my/update")
    public String updateForm(Model model) {
//        log.info("user22={}", authentication.getName());
        /*UserEntity user = userRepository.findByUserName(authentication.getName()).orElseThrow(() -> {
            throw new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage());
        });*/
//        model.addAttribute("user", user);
        // 로그인이 되어있는 유저의 id와 수정페이지에 접속하는 id가 같아야 함
        return "user/updateForm";
    }

    @PostMapping("/users/my/update")
    public String update(/*@PathVariable Long id, UserEntity user*/) {
//        userService.updateUser()
        return "user/updateForm";
    }

    /*public String admin(Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserResponse> userList = userService.findAll(pageable);
        model.addAttribute("userList", userList);
        return "user/admin";
    }*/

    @GetMapping("/users/my/orders")
    public String myOrders(Model model, OrderSearch orderSearch,
                           @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = "jin2"; // 임시
        List<OrderInfoResponse> orderList = orderService.findAllOrder2(userName, orderSearch);
        model.addAttribute("orderSearch", orderSearch);
        model.addAttribute("orderList", orderList);
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
