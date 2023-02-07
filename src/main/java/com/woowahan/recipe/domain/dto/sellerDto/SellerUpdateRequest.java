package com.woowahan.recipe.domain.dto.sellerDto;

import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpdateRequest {

    private String sellerName;
    private String companyName;
    private String address;
    private String email;
    private String phoneNum;

}
