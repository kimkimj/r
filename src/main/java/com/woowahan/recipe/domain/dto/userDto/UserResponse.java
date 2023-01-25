package com.woowahan.recipe.domain.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowahan.recipe.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
//    private Long id;
    private String userName;
    private String password;
    private String name;
    private String address;
    private String email;
    private String phoneNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
    private String birth;

    public static UserResponse toUserResponse(UserEntity user) {
        return UserResponse.builder()
//                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .name(user.getName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .birth(user.getBirth())
                .build();
    }
}
