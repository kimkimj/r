package com.woowahan.recipe.domain.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewDeleteResponse {
   private Long reviewId;
   private String message;
}
