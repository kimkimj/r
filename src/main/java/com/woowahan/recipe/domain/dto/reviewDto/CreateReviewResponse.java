package com.woowahan.recipe.domain.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewResponse {
    private Long reivew_id;
    private String nickname;
    private String comment;
}
