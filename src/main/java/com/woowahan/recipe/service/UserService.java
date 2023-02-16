package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.UserRepository;
import com.woowahan.recipe.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

    private long expiredTimeMs = 2 * 60 * 60 * 1000; // 토큰 유효시간: 2시간


    /**
     * 중복로직 리팩토링 - 2/6 11:10pm
     */
    //[중복 로직] user 존재 확인 + 가져오기
    public UserEntity validateUser(String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        return user;
    }


    public UserJoinResDto join(UserJoinReqDto userJoinReqDto) {

        // userName(ID) 중복확인
        userRepository.findByUserName(userJoinReqDto.getUserName())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        // email 중복확인
        userRepository.findByEmail(userJoinReqDto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage());
                });

        UserEntity savedUser = userRepository.save(userJoinReqDto.toEntity(
                encoder.encode(userJoinReqDto.getPassword())));

        return new UserJoinResDto(savedUser.getUserName(), String.format("%s님의 회원가입이 완료되었습니다.", savedUser.getUserName()));


    }

    public String login(String userName, String password) {

        // userName(ID)가 없는 경우
        UserEntity user = validateUser(userName);

        // password가 맞지 않는 경우
        if(!encoder.matches(password, user.getPassword())) { // 날것과 DB(복호화된 패스워드)를 비교
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtTokenUtils.createToken(userName, secretKey, expiredTimeMs);
    }

    /**
     * 회원정보 조회 - One Person
     */
    @Transactional(readOnly = true)
    public UserResponse findUser(Long id) {

        // 찾고자 하는 회원의 id가 없는 경우
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        return UserResponse.toUserResponse(user);
    }

    // TODO: 2023-01-21 회원가입 수정에 비밀번호 로직 변경
    /**
     * 회원정보 수정
     */
    @Transactional // 이 메서드를 종료할 경우 수정된 정보가 commit 또는 rollback이 되도록 설정
    public UserResponse updateUser(Long id, UserResponse userResponse, String userName) {

        // 고유 식별 번호(id)가 없는 경우
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // ID(userName)가 없는 경우
        validateUser(userName);

        // TODO: 2023-01-24 생각해봐야 할 사항 - 애초에 ID는 수정이 되면 안된다.
        // 회원가입과 동일하게 정보 수정시에도 ID(userName)이 중복되지 않게 처리
        userRepository.findByUserName(userResponse.getUserName())
                .ifPresent(userEntity -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        // 회원가입과 동일하게 정보 수정시에도 email이 중복되지 않게 처리
        userRepository.findByEmail(userResponse.getEmail())
                .ifPresent(userEntity -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage());
                });

        // 본인인 경우 수정 가능, ROLE이 ADMIN이면 수정 가능
        if(!user.getUserName().equals(userName) && user.getUserRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        user.updateUser(userResponse.getUserName(), userResponse.getPassword(), userResponse.getName(),
                        userResponse.getAddress(),userResponse.getEmail(), userResponse.getPhoneNum(), userResponse.getBirth());

        userRepository.save(user);

        return UserResponse.toUserResponse(user);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public UserDeleteDto deleteUser(Long id, String userName) {

        // 찾고자 하는 회원의 고유번호 id가 없는 경우
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 본인인 경우 삭제 가능, ROLE이 ADMIN이면 삭제 가능
        if(!user.getUserName().equals(userName) && user.getUserRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        userRepository.delete(user);
        return new UserDeleteDto(id, "회원 삭제가 완료되었습니다.");
    }

    /**
     * 마이페이지 - 회원정보 조회
     */
    public UserResponse findMyPage(Long id) {

        // 찾고자 하는 회원의 id가 없는 경우
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        return UserResponse.toUserResponse(user);
    }

    /**
     * 마이페이지 - 회원정보 수정
     */
    @Transactional
    public UserUpdateDto updateMyPage(Long id, UserUpdateDto userUpdateDto, String userName) {

        // 고유 식별 번호(id)가 없는 경우
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 애초에 ID는 수정이 되면 안되기 때문에 ID(userName) 중복체크 필요없음

        // 회원가입과 동일하게 정보 수정시에도 email이 중복되지 않게 처리
        userRepository.findByEmail(userUpdateDto.getEmail())
                .ifPresent(userEntity -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage());
                });

        // 본인인 경우 수정 가능, ROLE이 ADMIN이면 수정 가능
        if (!user.getUserName().equals(userName) && user.getUserRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        user.updateMyPage(encoder.encode(userUpdateDto.getPassword()), userUpdateDto.getName(), userUpdateDto.getAddress(),
                userUpdateDto.getEmail(), userUpdateDto.getPhoneNum(), userUpdateDto.getBirth());

        userRepository.save(user);

        return UserUpdateDto.toUserUpdateDto(user);
    }

    @Transactional
    public UserResponse updateInfo(UserUpdateReqDto reqDto, String userName) {
        UserEntity user = validateUser(userName);

        user.updateInfo(reqDto);
        return UserResponse.toUserResponse(user);
    }

    @Transactional
    public UserResponse updatePassword(String password, String userName) {
        UserEntity user = validateUser(userName);

        user.updatePassword(encoder.encode(password));
        return UserResponse.toUserResponse(user);
    }

    public Long findUserId (String username) {
        UserEntity user = validateUser(username);
        return user.getId();
    }

    public boolean checkUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}
