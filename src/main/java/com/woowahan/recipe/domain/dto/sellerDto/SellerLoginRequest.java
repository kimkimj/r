package com.woowahan.recipe.domain.dto.sellerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerLoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String sellerName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
