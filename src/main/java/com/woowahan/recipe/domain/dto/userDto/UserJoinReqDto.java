package com.woowahan.recipe.domain.dto.userDto;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinReqDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "아이디는 8자 이하 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{8,24}$", message = "비밀번호는 8~24자 한글, 영문 대소문자, 숫자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상 16자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNum;

//    private UserRole userRole;

    @NotBlank(message = "생년월일을 입력해주세요.")
    private Date birth;

    // UserJoinReqeust가 User로 변환되는 로직
    // 정보에 필요한 폼들이 다 들어가야 한다.
    public UserEntity toEntity(String password) {
        return UserEntity.builder()
                .userName(userName)
                .password(password)
                .name(name)
                .address(address)
                .email(email)
                .phoneNum(phoneNum)
                .userRole(UserRole.USER)
                .birth(birth)
                .build();
    }
}
