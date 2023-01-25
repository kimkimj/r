package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateResponse;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.ReviewRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {
    ReviewService reviewService;

    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    private final Long userId = 1L;
    private final String userName = "BaekJongWon";
    private final UserEntity user = UserEntity.builder()
            .id(userId)
            .userName(userName)
            .build();

    /**
     * 다른 유저
     */
    private final Long anotherUserId = 2L;
    private final String anotherUsername = "GordonRamsay";
    private final UserEntity anotherUser = UserEntity.builder()
            .id(anotherUserId)
            .userName(anotherUsername)
            .build();


    // 레시피엔티티 생성
    private final Long recipeId = 1l;
    private final String title = "유부초밥";
    private final String body = "이렇게";
    private final Integer like = 10;
    private final Integer view = 12;
    private final RecipeEntity recipe = RecipeEntity.builder()
            .id(recipeId)
            .recipe_title(title)
            .recipe_body(body)
            .user(user)
            .recipe_like(like)
            .recipe_view(view)
            .build();

    // review entity 생성
    private final Long reviewId = 1l;
    private final String review_comment = "너무 맛있어용";

    private final ReviewEntity review = ReviewEntity.builder()
            .recipe(recipe)
            .reviewId(reviewId)
            .review_comment(review_comment)
            .user(user)
            .build();

    @BeforeEach
    void beforEach() {
        reviewService = new ReviewService(userRepository, recipeRepository, reviewRepository, publisher);
    }

    @Nested
    @DisplayName("리뷰 등록")
    class createReview {

        @Test
        @DisplayName("리뷰 등록 성공")
        void create_review_success() {

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            when(reviewRepository.save(any())).thenReturn(review);

            assertDoesNotThrow(() -> reviewService.createReview(
                    recipeId, new ReviewCreateRequest(review_comment), userName));
        }

        // TODO: 2023-01-19 로그인하지 않았을 때 에러코드: invalid permission 아니면 username not found?
        @Test
        @DisplayName("리뷰 등록 실패 - 로그인 하지 않은 경우")
        void create_review_fail() {

            when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            when(reviewRepository.save(any())).thenReturn(review);

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.createReview(
                    recipeId, new ReviewCreateRequest(review_comment), userName));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
        }
    }


    @Nested
    @DisplayName("리뷰 수정")
    class updateReview {
        @Test
        @DisplayName("리뷰 수정 성공")
        void update_review_success() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            ReviewCreateResponse reviewCreateResponse= reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, userName);
            assertThat(reviewCreateResponse.getComment()).isEqualTo("댓글 수정");
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 해당 레시피가 없는 경우")
        void update_review_fail() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.empty());

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, userName));
            assertEquals(ErrorCode.RECIPE_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 해당 유저가 없는 경우")
        void update_review_fail2() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.of(recipe));

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, userName));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 작성자와 유저가 일치하지 않는 경우")
        void update_review_fail3() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.of(recipe));

            when(userRepository.findByUserName(anotherUsername))
                    .thenReturn(Optional.of(anotherUser));

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, anotherUsername));
            assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
        }
    }


}


