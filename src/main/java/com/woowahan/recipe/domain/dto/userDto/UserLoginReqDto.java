package com.woowahan.recipe.domain.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReqDto {
    private String userName;
    private String password;
}
