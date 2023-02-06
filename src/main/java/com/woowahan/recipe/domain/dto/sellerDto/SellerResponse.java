package com.woowahan.recipe.domain.dto.sellerDto;

import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private String sellerName;
    private String password;
    private String companyName;
    private String address;
    private String email;
    private String phoneNum;
    private String businessRegNum;

    private String message;


    public static SellerResponse toSellerResponse(SellerEntity seller) {
        return SellerResponse.builder()
                .sellerName(seller.getSellerName())
                .password(seller.getPassword())
                .companyName(seller.getCompanyName())
                .address(seller.getAddress())
                .email(seller.getEmail())
                .phoneNum(seller.getPhoneNum())
                .businessRegNum(seller.getBusinessRegNum())
                .message("수정이 완료되었습니다.")
                .build();
    }
}


