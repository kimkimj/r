package com.woowahan.recipe.domain.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateResponse {
    private Long reivew_id;
    private String username;
    private String comment;
}
