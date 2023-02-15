package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewGetResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewUpdateResponse;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.service.FindService;
import com.woowahan.recipe.service.ReviewService;
import com.woowahan.recipe.service.UserService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final FindService findService;


    // 회원이나 판매자로 로그인
    @GetMapping("/login")
    public String chooseLoginType() {
        return "loginType";
    }

    // 회원이나 판매자로 회원가입
    @GetMapping("/join")
    public String chooseJoinType() {
        return "joinType";
    }


    // 회원가입
    @GetMapping("/users/join")
    public String joinForm(Model model) {
        model.addAttribute("userJoinReqDto", new UserJoinReqDto());
        return "user/joinForm";
    }

    @PostMapping("/users/join")
    public String join(@Valid UserJoinReqDto form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/joinForm";
        }
        model.addAttribute("userJoinReqDto", new UserJoinReqDto());
        try {
            userService.join(form);
        } catch (AppException e) {
            model.addAttribute("e", e.getMessage());
        }
        return "redirect:/login";
    }

    // 로그인
    @GetMapping("/users/login")
    public String loginForm(Model model) {
        model.addAttribute("userLoginReqDto", new UserLoginReqDto());
        return "user/loginForm";
    }

    @PostMapping("/users/login")
    public String login(@Valid @ModelAttribute UserLoginReqDto userLoginReqDto, BindingResult result,
                        HttpServletResponse response){

        if (result.hasErrors()) {
            return "user/loginForm";
        }

        try {
            String token = userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
            CookieGenerator cookieGenerator = new CookieGenerator();
            cookieGenerator.setCookieName("token");
            cookieGenerator.setCookieHttpOnly(true);
            cookieGenerator.addCookie(response, token);
            cookieGenerator.setCookieMaxAge(60 * 60 * 2);

        } catch (AppException e) {
            return "user/loginForm";
        }

        return "redirect:/";
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletResponse response) {

        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName("token");
        cookieGenerator.addCookie(response, "deleted");
        cookieGenerator.setCookieMaxAge(0);

        return "redirect:/";
    }

    // 마이페이지
    @GetMapping("/users/my")
    public String myPage(Model model, Authentication authentication) {
        UserResponse user = findService.findUserName(authentication.getName());
        model.addAttribute("user", user);
        return "user/my/myInfo";
    }

    // 회원 정보 수정
    @GetMapping("/users/my/update")
    public String updateForm(Model model, Authentication authentication) {
        UserResponse user = findService.findUserName(authentication.getName());
        model.addAttribute("user", user);
        // 로그인이 되어있는 유저의 id와 수정페이지에 접속하는 id가 같아야 함
        return "user/updateForm";
    }

    @PostMapping("/users/my/update")
    public String updateUser(Model model, UserUpdateReqDto reqDto, Authentication authentication) {
        UserResponse userResponse = userService.updateInfo(reqDto, authentication.getName());
        model.addAttribute("user", userResponse);

        return "redirect:/users/my";
    }

    @GetMapping("/users/my/update/password")
    public String updatePasswordForm(Model model) {
        model.addAttribute("userPasswordReqDto", new UserPasswordReqDto());
        return "user/my/passwordForm";
    }

    @PostMapping("/users/my/update/password")
    public String updatePassword(Model model, UserPasswordReqDto userPasswordReqDto, Authentication authentication) {
        model.addAttribute("userPasswordReqDto", userPasswordReqDto);
        userService.updatePassword(userPasswordReqDto.getPassword(), authentication.getName());
        return "redirect:/users/my";
    }

    // 내가 작성한 리뷰 목록
    @GetMapping("/users/my/reviews")
    public String myReviews(Model model, Authentication authentication,
                            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = authentication.getName();
        Page<ReviewListResponse> reviewList = reviewService.findAllReviewsByUser(username, pageable);

        int nowPage = reviewList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, reviewList.getTotalPages());
        int lastPage = reviewList.getTotalPages();

        model.addAttribute("allMyReviews", reviewList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "user/my/myReviews";
    }

    // 리뷰 수정
    @GetMapping("/update/{recipeId}/{reviewId}")
    public String updateReview(@PathVariable Long recipeId, @PathVariable Long reviewId, Model model) {
        ReviewGetResponse review = reviewService.findReviewById(reviewId);
        model.addAttribute("review", review);
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("reviewId", reviewId);
        return "review/updateForm";
    }

    @PostMapping("/update/{recipeId}/{reviewId}")
    public String update(@PathVariable Long recipeId, @PathVariable Long reviewId,
                         ReviewCreateRequest request, Authentication authentication, Model model) {
        String username = authentication.getName();
        ReviewUpdateResponse reviewUpdateResponse = reviewService.updateReview(recipeId, reviewId, request, username);
        model.addAttribute("review", reviewUpdateResponse);
        return "redirect:/users/my/reviews";
    }

    // 리뷰 삭제
    @GetMapping("/delete/{recipeId}/{reviewId}")
    public String delete(@PathVariable Long recipeId, @PathVariable Long reviewId, Authentication authentication) {
        String username = authentication.getName();
        reviewService.deleteReview(recipeId, reviewId, username);
        return "redirect:/users/my/reviews";
    }

    @GetMapping("/users/my/recipe-like")
    public String myLikeRecipe(Model model, Authentication authentication, @PageableDefault(size = 20)Pageable pageable) {
        Page<RecipeFindResDto> myLikeRecipeList = findService.findMyLikeRecipe(authentication.getName(), pageable);
        model.addAttribute("myLikeRecipeList", myLikeRecipeList);
        return "user/my/myLikeRecipe";
    }
}
