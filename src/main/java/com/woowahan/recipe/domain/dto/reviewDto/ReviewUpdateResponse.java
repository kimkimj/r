package com.woowahan.recipe.domain.dto.reviewDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateResponse {
    private Long reviewId;
    private String message;
}
