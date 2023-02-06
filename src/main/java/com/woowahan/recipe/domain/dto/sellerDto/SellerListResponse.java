package com.woowahan.recipe.domain.dto.sellerDto;


import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerListResponse {
    private String sellerName;
    private String companyName;
    private String address;
    private String email;
    private String phoneNum;
    private String businessRegNum;


    public static SellerListResponse from(SellerEntity seller) {
        return SellerListResponse.builder()
                .sellerName(seller.getSellerName())
                .companyName(seller.getCompanyName())
                .address(seller.getAddress())
                .email(seller.getEmail())
                .phoneNum(seller.getPhoneNum())
                .businessRegNum(seller.getBusinessRegNum())
                .build();
    }
}