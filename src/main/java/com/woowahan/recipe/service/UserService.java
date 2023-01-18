package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserJoinResDto;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.exception.ErrorResult;
import com.woowahan.recipe.repository.UserRepository;
import com.woowahan.recipe.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

    private long expiredTimeMs = 60 * 60 * 1000; // 토큰 유효시간: 1시간

    public UserJoinResDto join(UserJoinReqDto userJoinReqDto) {

        // userName(ID) 중복확인
        userRepository.findByUserName(userJoinReqDto.getUserName())
                .ifPresent(user -> {
                    throw new ErrorResult(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        // email 중복확인
        userRepository.findByEmail(userJoinReqDto.getEmail())
                .ifPresent(user -> {
                    throw new ErrorResult(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage());
                });

        UserEntity savedUser = userRepository.save(userJoinReqDto.toEntity(
                encoder.encode(userJoinReqDto.getPassword())));

        return new UserJoinResDto(savedUser.getUserName(), String.format("%s님의 회원가입이 완료되었습니다.", savedUser.getUserName()));

        // 위 코드 다른 로직
        /*UserJoinResDto dto = userRepository.save(userJoinReqDto.toEntity(
                encoder.encode(userJoinReqDto.getPassword()))) // userEntity 객체 (이미 정보를 담고있다)
                .toUserJoinResDto("님의 회원가입이 완료되었습니다.");

        return dto;*/
    }

    public String login(String userName, String password) {

        // userName(ID)가 없는 경우
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ErrorResult(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        // password가 맞지 않는 경우
        if(!encoder.matches(password, user.getPassword())) { // 날것과 DB(복호화된 패스워드)를 비교
            throw new ErrorResult(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtTokenUtils.createToken(userName, secretKey, expiredTimeMs);
    }
}
