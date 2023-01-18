package com.woowahan.recipe.controller;

import com.woowahan.recipe.domain.dto.reviewDto.CreateReviewRequest;
import com.woowahan.recipe.domain.dto.reviewDto.CreateReviewResponse;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class ReviewController {
    private final ReviewService reviewServce;

    @PostMapping("/{id}/reviews")
    public Response<CreateReviewResponse> createReview(@PathVariable Long recipeId, @RequestBody CreateReviewRequest createReviewRequest, @ApiIgnore Authentication authentication) {
        CreateReviewResponse createReviewResponse = reviewServce.create(recipeId, createReviewRequest, authentication.getName());
        return Response.success(createReviewResponse);
    }



}
