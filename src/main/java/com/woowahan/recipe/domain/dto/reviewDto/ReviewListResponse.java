package com.woowahan.recipe.domain.dto.reviewDto;

import com.woowahan.recipe.domain.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewListResponse {
    private Long reviewId;
    private Long recipeId;
    private String recipeTitle;
    private String username;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    public static ReviewListResponse toList(ReviewEntity review) {
        return ReviewListResponse.builder()
                .reviewId(review.getReviewId())
                .recipeId(review.getRecipe().getId())
                .recipeTitle(review.getRecipe().getRecipeTitle())
                .username(review.getUser().getUserName())
                .content(review.getContent())
                .createdDate(review.getCreatedDate())
                .lastModified(review.getLastModifiedDate())
                .build();
    }
}

