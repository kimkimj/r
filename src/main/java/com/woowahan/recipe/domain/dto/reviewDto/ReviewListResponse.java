package com.woowahan.recipe.domain.dto.reviewDto;

import com.woowahan.recipe.domain.dto.itemDto.ItemListResDto;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewListResponse {
    private Long reviewId;
    private String username;
    private String review_comment;
    private LocalDateTime createdDate;
    private LocalDateTime last_modified;

    public static ReviewListResponse toList(ReviewEntity review) {
        return ReviewListResponse.builder()
                .reviewId(review.getReviewId())
                .username(review.getUser().getUserName())
                .review_comment(review.getReview_comment())
                .createdDate(review.getCreatedDate())
                .last_modified(review.getLastModifiedDate())
                .build();
    }
}

