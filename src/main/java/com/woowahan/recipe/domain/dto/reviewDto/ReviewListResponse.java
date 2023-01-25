package com.woowahan.recipe.domain.dto.reviewDto;

import com.woowahan.recipe.domain.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewListResponse {
    private Long reviewId;
    private String username;
    private String reviewComment;
    private LocalDateTime createdDate;
    private LocalDateTime last_modified;

    public static ReviewListResponse toList(ReviewEntity review) {
        return ReviewListResponse.builder()
                .reviewId(review.getReviewId())
                .username(review.getUser().getUserName())
                .reviewComment(review.getReviewComment())
                .createdDate(review.getCreatedDate())
                .last_modified(review.getLastModifiedDate())
                .build();
    }
}

