package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.userDto.UserJoinReqDto;
import com.woowahan.recipe.domain.dto.userDto.UserJoinResDto;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean // 가짜 객체를 만들어 컨테이너가 주입할 수 있도록 한다.
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원가입 테스트")
    class Join {

        // given
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .userName("user")
                .password("1234")
                .name("user")
                .address("서울시")
                .email("user@gmail.com")
                .phoneNum("01012345678")
                .birth("20220919")
                .build();

        @Test
        @WithMockUser
        void 회원가입_성공() throws Exception {

            UserJoinResDto userJoinResDto = new UserJoinResDto(userJoinReqDto.getUserName(), "회원가입 성공");

            // when
            when(userService.join(any()))
                    .thenReturn(userJoinResDto);

            // then
            mockMvc.perform(post("/api/v1/users/join")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").exists());
        }

        @Test
        @WithMockUser
        void 회원가입_실패_아이디중복() throws Exception {

            // when
            when(userService.join(any()))
                    .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, "아이디 중복으로 인한 실패"));

            // then
            mockMvc.perform(post("/api/v1/users/join")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                    .andDo(print())
                    .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getHttpStatus().value()));
        }

        @Test
        @WithMockUser
        void 회원가입_실패_이메일중복() throws Exception {

            // when
            when(userService.join(any()))
                    .thenThrow(new AppException(ErrorCode.DUPLICATED_EMAIL, "이메일 중복으로 인한 실패"));

            // then
            mockMvc.perform(post("/api/v1/users/join")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                    .andDo(print())
                    .andExpect(status().is(ErrorCode.DUPLICATED_EMAIL.getHttpStatus().value()));
        }
    }
}