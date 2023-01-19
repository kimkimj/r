package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
