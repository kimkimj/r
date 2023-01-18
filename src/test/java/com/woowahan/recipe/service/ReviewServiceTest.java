package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.reviewDto.CreateReviewRequest;
import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.repository.AlarmRepository;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.ReviewRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {
    /*

    ReviewService reviewService;

    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);

    // 카트 엔티티 생성
    CartEntity cart = new CartEntity();

    //유저 엔티티 생성
    private final Long userId = 15l;
    private String userName = "sugaboy";
    private final String nickanme = "백종원";
    private String password = "1234";
    private String address = "서울시";
    private String email = "jongwon@gmail.com";
    private String phoneNumber = "010-1234-5678";
    private String userRole = "USER";
    private Date birth = new Date(2023, 01, 28);
    private final UserEntity userEntity = UserEntity.builder()
            .userId(userId)
            .userName(userName)
            .nickname(nickanme)
            .password(password)
            .address(address)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(userRole)
            .birth(birth)
            .cartEntity(cart)
            .build();

    // 레시피엔티티 생성
    private final Long recipeId = 1l;
    private final String title = "유부초밥";
    private final String body = "이렇게";
    private final Long like = 10l;
    private final Long view = 12l;
    private final RecipeEntity recipeEntity = RecipeEntity.builder()
            .recipeId(recipeId)
            .recipe_title(title)
            .recipe_body(body)
            .userId(userEntity)
            .recipe_like(like)
            .recipe_view(view)
            .build();

    // review entity 생성
    private final Long reviewId = 1l;
    private final String review_comment = "너무 맛있어용";

    private final ReviewEntity reviewEntity = ReviewEntity.builder()
            .recipe(recipeEntity)
            .reviewId(reviewId)
            .review_comment(review_comment)
            .user(userEntity)
            .build();

    @BeforeEach
    void beforEach() {
        reviewService = new ReviewService(userRepository, recipeRepository, reviewRepository, alarmRepository);
    }

    @Test
    @DisplayName("Create review: success")
    void create_review_success() {

        when(userRepository.findByUsername(userName))
                .thenReturn(Optional.of(userEntity));
        when(recipeRepository.save(any()))
                .thenReturn(recipeEntity);
        when(reviewRepository.save(any()))
                .thenReturn(reviewEntity);

        Assertions.assertDoesNotThrow(() -> reviewService.createReview(
                recipeId, new CreateReviewRequest(review_comment), userName));
    }
    */
}

