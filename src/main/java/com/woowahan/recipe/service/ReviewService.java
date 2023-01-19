package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewDeleteResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.AlarmRepository;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.ReviewRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ReviewRepository reviewRepository;
    private final AlarmRepository alarmRepository;

    // User가 존재하는지 확인한다
    private UserEntity validateUser(String username) {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        return user;
    }

    // 댓글을 달 레시피가 존재하는지 확인한다
    private RecipeEntity validateRecipe(Long recipeId)  {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        return recipe;
    }

    // 리뷰가 존재하는지 확인
    private ReviewEntity validateReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND.getMessage()));
        return review;
    }

    // 리뷰 작성
    public ReviewCreateResponse createReview(Long recipeId, ReviewCreateRequest reviewCreateRequest, String username)  {
        // 유저가 존재하는지 확인
        UserEntity user = validateUser(username);

        // 레시피가 존재하는지 확인
        RecipeEntity recipe = validateRecipe(recipeId);

        // 내용이 있는지 확인. 없으면 에러 코드
        if (reviewCreateRequest.getComment().length() == 0) {
            throw new AppException(ErrorCode.EMPTY_CONTENT, ErrorCode.EMPTY_CONTENT.getMessage());
        }

        // 리뷰 저장
        ReviewEntity review = reviewRepository.save(reviewCreateRequest.toEntity(user, recipe, reviewCreateRequest.getComment()));

        // 알람 울리도록 저장
        // alarm entity로 바꾼 후 alarm repository에 저장

        return new ReviewCreateResponse(review.getReviewId(), user.getName(), review.getReview_comment());
    }

    // 리뷰 수정
    public ReviewCreateResponse updateReview(Long recipeId, Long reviewId, ReviewCreateRequest reviewCreateRequest, String username) {
        // 유저가 존재하는지 확인
        UserEntity user = validateUser(username);

        // 레시피가 존재하는지 확인
        RecipeEntity recipe = validateRecipe(recipeId);

        //리뷰 작성자와 유저가 동일한지 확인
        if (!recipe.getUser().getUserName().equals(username)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        // 리뷰가 존재하는지 확인
        ReviewEntity review = validateReview(reviewId);

        // 내용이 있는지 확인
        if (reviewCreateRequest.getComment().length() == 0) {
            throw(new AppException(ErrorCode.EMPTY_CONTENT, ErrorCode.EMPTY_CONTENT.getMessage()));
        }

        review.update(reviewCreateRequest.getComment());

        // 저장
        ReviewEntity savedReview = reviewRepository.save(review);

        // 알람 울리도록 저장
        // alarm entity로 바꾼 후 alarm repository에 저장
        //alarmRepository.save();

        return new ReviewCreateResponse(savedReview.getReviewId(), user.getName(), savedReview.getReview_comment());
    }

    // 리뷰 단건 삭제
   public ReviewDeleteResponse deleteReview(Long recipeId, Long reviewId, String username) {
       // 유저가 존재하는지 확인
       UserEntity user = validateUser(username);

       // 레시피가 존재하는지 확인
       RecipeEntity recipe = validateRecipe(recipeId);

       //관리자거나 리뷰 작성자와 유저가 동일한지 확인
       if (user.getUserRole() == UserRole.HEAD || user.getUserRole() == UserRole.ADMIN ||
       !recipe.getUser().getUserName().equals(username)) {
           throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
       }

       // soft delete
       reviewRepository.deleteById(reviewId);
       return new ReviewDeleteResponse(reviewId, "댓글 삭제 완료");
   }

/*
    public ReviewListResponse findAllReviewsByRecipe(Long recipeId) {
        // 레시피가 존재하는지 확인
        /*RecipeEntity recipe = validateRecipe(recipeId);

        List<ReviewEntity> list = reviewRepository.findAllByRecipe(recipe);
        List<PostGetResponse> postListResponse = list.map(lists -> PostGetResponse.builder()
                        .id(lists.getPostId())
                        .title(lists.getTitle())
                        .body(lists.getBody())
                        .userName(lists.getUser().getUsername())
                        .createdAt(lists.getCreatedAt())
                        .lastModifiedAt(lists.getLastModifiedAt())
                        .build())
                .toList();

        return PostListResponse.builder()
                .list(postListResponse)
                .build();
    }*/


}
