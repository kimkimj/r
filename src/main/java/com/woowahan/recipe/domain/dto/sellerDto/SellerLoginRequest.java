package com.woowahan.recipe.domain.dto.sellerDto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerLoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String sellerName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
