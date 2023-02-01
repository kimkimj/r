package com.woowahan.recipe.domain.dto.userDto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReqDto {

    @NotBlank(message = "아이디를 입력해주세요.")
//    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "아이디는 8자 이하 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
//    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{8,24}$", message = "비밀번호는 8~24자 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String password;
}
