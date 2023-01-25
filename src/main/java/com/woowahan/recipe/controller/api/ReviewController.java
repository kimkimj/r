package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.reviewDto.*;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{recipeId}/reviews")
    public Response<Page<ReviewListResponse>> getAllReviews(@PathVariable Long recipeId, Pageable pageable) {
            Page<ReviewListResponse> reviews = reviewService.findAllReviews(recipeId, pageable);
            return Response.success(reviews);
    }

    // 특정 유저에 대한 리뷰 조회
    @GetMapping("/reviews")
    public Response<Page<ReviewListResponse>> getAllReviewsByUser(Pageable pageable, Authentication authentication) {
        Page<ReviewListResponse> reviews = reviewService.findAllReviewsByUser(authentication.getName(), pageable);
        return Response.success(reviews);
    }

    @PostMapping("/{recipeId}/reviews")
    public Response<ReviewCreateResponse> createReview(@PathVariable Long recipeId,
                                                     @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                     Authentication authentication) {
        ReviewCreateResponse reviewCreateResponse = reviewService.createReview(recipeId, reviewCreateRequest, authentication.getName());
        return Response.success(reviewCreateResponse);
    }

    @PutMapping("/{recipeId}/reviews/{reviewId}")
    public Response<ReviewUpdateResponse> updateReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                       Authentication authentication) {
        ReviewUpdateResponse reviewUpdateResponse = reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, authentication.getName());
        return Response.success(reviewUpdateResponse);
    }

    @DeleteMapping ("/{recipeId}/reviews/{reviewId}")
    public Response<ReviewDeleteResponse> deleteReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       Authentication authentication) {
        ReviewDeleteResponse reviewDeleteResponse = reviewService.deleteReview(recipeId, reviewId, authentication.getName());
        return Response.success(reviewDeleteResponse);
    }

}
