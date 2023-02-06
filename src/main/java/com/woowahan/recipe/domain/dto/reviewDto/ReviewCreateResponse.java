package com.woowahan.recipe.domain.dto.reviewDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateResponse {
    private Long reviewId;
    private String username;
    private String content;
}
