package com.woowahan.recipe.domain.dto.sellerDto;


import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.SellerEntity;
import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerJoinRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "아이디는 8자 이하 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String sellerName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{8,24}$", message = "비밀번호는 8~24자 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String password;

    @NotBlank(message = "회사명을 입력해주세요")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String companyName;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNum;

    @NotBlank(message = "사업자 등록번호를 입력해주세요.")
    private String businessRegNum;

    private final UserRole userRole = UserRole.SELLER;


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
