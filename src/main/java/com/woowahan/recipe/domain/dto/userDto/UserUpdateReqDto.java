package com.woowahan.recipe.domain.dto.userDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateReqDto {

    private String address;
    private String email;
    private String phoneNum;

    @Builder
    public UserUpdateReqDto(String address, String email, String phoneNum) {
        this.address = address;
        this.email = email;
        this.phoneNum = phoneNum;
    }
}
