package com.woowahan.recipe.domain.dto.sellerDto;


import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerJoinRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String sellerName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "회사명을 입력해주세요")
    private String companyName;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNum;

    @NotBlank(message = "사업자 등록번호를 입력해주세요.")
    private String businessRegNum;


    public SellerEntity toEntity(String encodedPassword) {
        return SellerEntity.builder()
                .sellerName(sellerName)
                .password(encodedPassword)
                .companyName(companyName)
                .email(email)
                .phoneNum(phoneNum)
                .businessRegNum(businessRegNum)
                .build();
    }

}
