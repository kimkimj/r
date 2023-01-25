package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.reviewDto.*;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.AlarmRepository;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.ReviewRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {
    ReviewService reviewService;

    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    private final Long userId = 1L;
    private final String username = "BaekJongWon";
    private final UserEntity user = UserEntity.builder()
            .id(userId)
            .userName(username)
            .build();

    // 다른 유저
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
    private final int like = 10;
    private final int view = 12;
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
        reviewService = new ReviewService(userRepository, recipeRepository, reviewRepository, alarmRepository, publisher);
    }

    @Nested
    @DisplayName("리뷰 등록")
    class createReview {

        @Test
        @DisplayName("리뷰 등록 성공")
        void create_review_success() {

            when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            when(reviewRepository.save(any())).thenReturn(review);

            assertDoesNotThrow(() -> reviewService.createReview(
                    recipeId, new ReviewCreateRequest(review_comment), username));
        }

        @Test
        @DisplayName("리뷰 등록 실패 - 로그인 하지 않은 경우")
        void create_review_fail() {

            when(userRepository.findByUserName(username)).thenReturn(Optional.empty());
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            when(reviewRepository.save(any())).thenReturn(review);

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.createReview(
                    recipeId, new ReviewCreateRequest(review_comment), username));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
        }
    }


    @Nested
    @DisplayName("리뷰 수정")
    class updateReview {
        @Test
        @DisplayName("리뷰 수정 성공")
        void update_review_success() {
            when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
            when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            ReviewCreateResponse reviewCreateResponse= reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, username);
            assertThat(reviewCreateResponse.getComment()).isEqualTo("댓글 수정");

        }

        @Test
        @DisplayName("리뷰 수정 실패 - 해당 레시피가 없는 경우")
        void update_review_fail() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.empty());

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, username));
            assertEquals(ErrorCode.RECIPE_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 해당 유저가 없는 경우")
        void update_review_fail2() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(username)).thenReturn(Optional.empty());
            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.of(recipe));

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, username));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 작성자와 유저가 일치하지 않는 경우")
        void update_review_fail3() {
            ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("댓글 수정");
            when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));

            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.of(recipe));

            when(userRepository.findByUserName(anotherUsername))
                    .thenReturn(Optional.of(anotherUser));

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, anotherUsername));
            assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("리뷰 삭제")
    class deleteReview {
        @Test
        @DisplayName("리뷰 삭제 성공")
        void delete_review_success() {
            given(userRepository.findByUserName(username)).willReturn(Optional.of(user));
            given(recipeRepository.findById(recipeId)).willReturn(Optional.of(recipe));
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

            ReviewDeleteResponse reviewDeleteResponse= reviewService.deleteReview(recipeId, reviewId, username);
            assertThat(reviewDeleteResponse.getMessage()).isEqualTo("댓글 삭제 완료");
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 해당 유저가 존재하지 않음")
        void delete_review_fail_1() {
            given(userRepository.findByUserName(username)).willReturn(Optional.empty());

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.deleteReview(recipeId, reviewId, username));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 해당 레시피가 존재하지 않음")
        void delete_review_fail_2() {
            given(userRepository.findByUserName(username)).willReturn(Optional.of(user));
            given(recipeRepository.findById(recipeId)).willReturn(Optional.empty());

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.deleteReview(recipeId, reviewId, username));
            assertEquals(ErrorCode.RECIPE_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 작성자와 유저가 일치하지 않는 경우")
        void delete_review_fail_3() {
            given(userRepository.findByUserName(username)).willReturn(Optional.of(user));
            given(recipeRepository.findById(recipeId)).willReturn(Optional.of(recipe));
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

            when(userRepository.findByUserName(anotherUsername)).thenReturn(Optional.of(anotherUser));

            AppException exception = Assertions.assertThrows(AppException.class, () -> reviewService.deleteReview(recipeId, reviewId, anotherUsername));
            assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
        }
    }

    /*
    @Test
    @DisplayName("리뷰 전체 조회")
    void findAll_review_success() {

        ReviewEntity review2 = ReviewEntity.builder()
                .reviewId(2l)
                .recipe(recipe)
                .review_comment("comment1")
                .build();

        PageImpl<ReviewEntity> reviewList = new PageImpl<>(List.of(review, review2));
        PageRequest pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createdDate");

        given(reviewRepository.findAll(pageable)).willReturn(reviewList);

        given(recipeRepository.findById(recipeId)).willReturn(Optional.of(recipe));
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(reviewRepository.findById(2l)).willReturn(Optional.of(review2));
        //Page<ReviewListResponse> reviewListResponses = reviewService.findAllReviews(recipeId, pageable);
    }*/

}


