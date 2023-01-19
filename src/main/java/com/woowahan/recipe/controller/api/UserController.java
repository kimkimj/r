package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public Response<UserJoinResDto> join(@RequestBody UserJoinReqDto userJoinReqDto) {
        UserJoinResDto userJoinResDto = userService.join(userJoinReqDto);
        return Response.success(userJoinResDto);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public Response<UserLoginResDto> login(@RequestBody UserLoginReqDto userLoginReqDto) {
        String token = userService.login(userLoginReqDto.getUserName(), userLoginReqDto.getPassword());
        return Response.success(new UserLoginResDto(token));
    }

    // TODO: 2023-01-19 회원정보 조회, 수정, 삭제 구현

    /**
     * 회원정보 조회
     */
    @GetMapping("/{id}")
    public Response<UserResponse> findUser(@PathVariable Long id) {
        UserResponse userResponse = userService.findUser(id);
        return Response.success(userResponse);
    }

    /**
     * 회원정보 삭제 - soft Delete 이용
     */
    @DeleteMapping("/{id}")
    public Response<UserDeleteDto> deleteUser(@PathVariable Long id, Authentication authentication) {
        UserDeleteDto userDeleteDto = userService.deleteUser(id, authentication.getName());
        return Response.success(userDeleteDto);
    }
}
