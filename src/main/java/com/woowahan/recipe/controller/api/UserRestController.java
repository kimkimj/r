package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/users/join")
    public Response<UserJoinResDto> join(@RequestBody UserJoinReqDto userJoinReqDto) {
        UserJoinResDto userJoinResDto = userService.join(userJoinReqDto);
        return Response.success(userJoinResDto);
    }

    @GetMapping("/check-username")
    public String checkUserName(String userName) {
        if (!userService.checkUserName(userName)) {
            return "사용 가능한 아이디 입니다.";
        } else {
            return "중복된 아이디 입니다.";
        }
    }

    @GetMapping("/check-email")
    public String checkEmail(String email) {
        if (!userService.checkEmail(email)) {
            return "사용가능한 이메일 입니다.";
        } else {
            return "중복된 이메일 입니다.";
        }
    }



    /**
     * 로그인
     */
    @PostMapping("/users/login")
    public Response<UserLoginResDto> login(@RequestBody UserLoginReqDto userLoginReqDto) {
        String token = userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
        return Response.success(new UserLoginResDto(token));
    }

    /**
     * 회원정보 조회
     */
    @GetMapping("/users/{id}")
    public Response<UserResponse> findUser(@PathVariable Long id) {
        UserResponse userResponse = userService.findUser(id);
        return Response.success(userResponse);
    }

    /**
     * 회원정보 수정
     * UserUpdateDto를 만들어 쓰고자 했지만
     * UserResponse에 원하는 정보들이 들어있어서 UserResponse로 진행
     */
    @PutMapping("/users/{id}")
    public Response<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserResponse userResponse, Authentication authentication) {
        UserResponse userUpdResponse = userService.updateUser(id, userResponse, authentication.getName());
        return Response.success(userUpdResponse);
    }

    /**
     * 회원정보 삭제 - soft Delete 이용
     */
    @DeleteMapping("/users/{id}")
    public Response<UserDeleteDto> deleteUser(@PathVariable Long id, Authentication authentication) {
        UserDeleteDto userDeleteDto = userService.deleteUser(id, authentication.getName());
        return Response.success(userDeleteDto);
    }

    /**
     * 마이페이지 - 회원정보 조회
     */
    @GetMapping("/my/{id}")
    public Response<UserResponse> findMyPage(@PathVariable Long id) {
        UserResponse userResponse = userService.findMyPage(id);
        return Response.success(userResponse);
    }

    /**
     * 마이페이지 - 회원정보 수정
     */
    @PutMapping("/my/{id}")
    public Response<UserUpdateDto> updateMyPage(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        UserUpdateDto updateMe = userService.updateMyPage(id, userUpdateDto, authentication.getName());
        return Response.success(updateMe);
    }

}
