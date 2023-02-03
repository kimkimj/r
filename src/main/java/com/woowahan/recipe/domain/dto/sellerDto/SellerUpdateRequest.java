package com.woowahan.recipe.domain.dto.sellerDto;

import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpdateRequest {

    private String sellerName;
    private String password;
    private String companyName;
    private String address;
    private String email;
    private String phoneNum;
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
