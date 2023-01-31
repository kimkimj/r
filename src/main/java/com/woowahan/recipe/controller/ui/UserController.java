package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserLoginReqDto;
import com.woowahan.recipe.security.auth.PrincipalDetails;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.ReviewService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;

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
    public String login(@Valid @ModelAttribute UserLoginReqDto userLoginReqDto, BindingResult result) {
        if (result.hasErrors()) {
            result.getFieldErrors().stream().forEach(err ->
                    log.info("field={} value={} msg={}", err.getField(), err.getRejectedValue(), err.getDefaultMessage()));
            return "user/loginForm";
        }

        userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
        return "redirect:/";
    }

    // 회원 정보 수정
    @GetMapping("/users/my/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 수정페이지에 접속하는 id가 같아야 함
        if (principalDetails.getUser().getId() == id) {
            model.addAttribute("user", userService.findUser(id));
            return "user/updateForm";
        }
        return "redirect:/";
    }

    @PostMapping("/users/my/update/{id}")
    public String update(/*@PathVariable Long id, UserEntity user*/) {
//        userService.updateUser()
        return "user/updateForm";
    }

    @GetMapping("/users/my/orders")
    public String myOrders() {
        return "user/my/myOrder";
    }

    @GetMapping("/users/my/get-reviews")
    public String myGetReviews() {
        return "user/my/myGetReview";
    }


    @GetMapping("/users/my/reviews")
    public String myReviews(Model model, @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = "GordonRamsey"; // 인증 생기기 전까지 임시 사용
//        String username = authentication.getName();
        Page<ReviewListResponse> reviews = reviewService.findAllReviewsByUser(username, pageable);
        model.addAttribute("allMyReviews", reviews);
        return "user/my/myReviews";
    }

    @GetMapping("/update/{recipeId}/{reviewId}")
    public String updateReview(@PathVariable Long recipeId, @PathVariable Long reviewId, Model model) {
        model.addAttribute("reviewUpdateRequest", new ReviewCreateRequest());
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("reviewId", reviewId);
        return "review/updateForm";
    }

    @PostMapping("/update/{recipeId}/{reviewId}")
    public String update(@Valid @ModelAttribute ReviewCreateRequest request, BindingResult bindingResult,
                         @PathVariable Long recipeId, @PathVariable Long reviewId,
                        Authentication authentication, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "review/updateForm";
        }
        String username = "GordonRamsey"; // 인증 생기기 전까지 임시 사용
//        String username = authentication.getName();
        reviewService.updateReview(reviewId, recipeId, request, username);
        return "redirect:/users/my/myReviews";
    }

    @GetMapping("/delete/{recipeId}/{reviewId}")
    public String delete(@PathVariable Long recipeId, @PathVariable Long reviewId, Authentication authentication) {
        String username = "GordenRamsey"; // 인증 생기기 전까지 임시 사용
//        String username = authentication.getName();
        reviewService.deleteReview(reviewId, recipeId, username);
        return "redirect:/users/my/myReviews";
    }


    @GetMapping("/users/my/recipe-like")
    public String myLikeRecipe() {
        return "user/my/myLikeRecipe";
    }

}
