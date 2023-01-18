package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.reviewDto.CreateReviewRequest;
import com.woowahan.recipe.domain.dto.reviewDto.CreateReviewResponse;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.reviewDto.DeleteReviewResponse;
import com.woowahan.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
public class ReviewController {
    private final ReviewService reviewServce;

    @PostMapping("/{id}/reviews")
    public Response<CreateReviewResponse> createReview(@PathVariable Long recipeId,
                                                       @RequestBody CreateReviewRequest createReviewRequest,
                                                       Authentication authentication) {
        CreateReviewResponse createReviewResponse = reviewServce.createReview(recipeId, createReviewRequest, authentication.getName());
        return Response.success(createReviewResponse);
    }

    @PutMapping("/{id}/reviews/{reviewId}")
    public Response<CreateReviewResponse> createReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       @RequestBody CreateReviewRequest createReviewRequest,
                                                       Authentication authentication) {
        CreateReviewResponse createReviewResponse = reviewServce.updateReview(recipeId, reviewId, createReviewRequest, authentication.getName());
        return Response.success(createReviewResponse);
    }

    @DeleteMapping ("/{id}/reviews/{reviewId}")
    public Response<DeleteReviewResponse> createReview(@PathVariable Long recipeId, @PathVariable Long reviewId,
                                                       Authentication authentication) {
        DeleteReviewResponse deleteReviewResponse = reviewServce.deleteReview(recipeId, reviewId, authentication.getName());
        return Response.success(deleteReviewResponse);
    }

}
