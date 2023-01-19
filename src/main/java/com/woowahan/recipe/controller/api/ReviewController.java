package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateResponse;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewDeleteResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
public class ReviewController {
    private final ReviewService reviewService;

    public Response<ReviewCreateResponse> createReview(@PathVariable Long recipeId,
                                                     @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                     Authentication authentication) {
        ReviewCreateResponse reviewCreateResponse = reviewService.createReview(recipeId, reviewCreateRequest, authentication.getName());
        return Response.success(reviewCreateResponse);
    }

    @PutMapping("/{id}/reviews/{reviewId}")
    public Response<ReviewCreateResponse> updateReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                       Authentication authentication) {
        ReviewCreateResponse reviewCreateResponse = reviewService.updateReview(recipeId, reviewId, reviewCreateRequest, authentication.getName());
        return Response.success(reviewCreateResponse);
    }

    @DeleteMapping ("/{id}/reviews/{reviewId}")
    public Response<ReviewDeleteResponse> deleteReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       Authentication authentication) {
        ReviewDeleteResponse reviewDeleteResponse = reviewService.deleteReview(recipeId, reviewId, authentication.getName());
        return Response.success(reviewDeleteResponse);
    }

}
