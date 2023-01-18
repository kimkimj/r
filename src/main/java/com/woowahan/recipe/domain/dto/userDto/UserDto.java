package com.woowahan.recipe.domain.dto.userDto;

import com.woowahan.recipe.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private String address;
    private String email;
    private String phoneNum;
    private String userRole;
    private Date birth;

    public static UserDto toUserDto(UserEntity savedUser) {
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .name(savedUser.getName())
                .address(savedUser.getAddress())
                .email(savedUser.getEmail())
                .phoneNum(savedUser.getPhoneNum())
                .birth(savedUser.getBirth())
                .build();
    }
}
