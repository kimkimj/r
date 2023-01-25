package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.alarmDto.AlarmResponseDto;
import com.woowahan.recipe.domain.dto.userDto.*;
import com.woowahan.recipe.domain.entity.AlarmEntity;
import com.woowahan.recipe.domain.entity.AlarmType;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.fixture.AlarmEntityFixture;
import com.woowahan.recipe.fixture.RecipeEntityFixture;
import com.woowahan.recipe.fixture.UserEntityFixture;
import com.woowahan.recipe.service.AlarmService;
import com.woowahan.recipe.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@WithMockUser
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean // 가짜 객체를 만들어 컨테이너가 주입할 수 있도록 한다.
    UserService userService;

    @MockBean
    AlarmService alarmService;

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

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        // given
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto("user", "1234");

        @Test
        void 로그인_성공() throws Exception {

            UserLoginResDto userLoginResDto = new UserLoginResDto("jwt");

            // when
            when(userService.login(any(), any()))
                    .thenReturn(userLoginResDto.getJwt());
            // then
            mockMvc.perform(post("/api/v1/users/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userLoginReqDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").exists())
                    .andExpect(jsonPath("$.result.jwt").exists());
        }

        @Test
        void 로그인_실패_아이디없음() throws Exception {

            // when
            when(userService.login(any(), any()))
                    .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

            // then
            mockMvc.perform(post("/api/v1/users/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userLoginReqDto)))
                    .andDo(print())
                    .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()));
        }

        @Test
        void 로그인_실패_비밀번호다름() throws Exception {

            // when
            when(userService.login(any(), any()))
                    .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage()));

            // then
            mockMvc.perform(post("/api/v1/users/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userLoginReqDto)))
                    .andDo(print())
                    .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getHttpStatus().value()));
        }
    }

    @Nested
    @DisplayName("회원정보 조회 테스트")
    class FindUser {

        // given
        UserResponse userResponse = UserResponse.builder()
                .userName("user")
                .password("1234")
                .name("user")
                .address("서울시")
                .email("user@gmail.com")
                .phoneNum("01012345678")
                .birth("20220919")
                .build();

        @Test
        void 회원조회_성공() throws Exception {

            // when
            when(userService.findUser(any()))
                    .thenReturn(userResponse);

            // then
            mockMvc.perform(get("/api/v1/users/1")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.userName").exists())
                    .andExpect(jsonPath("$.result.password").exists())
                    .andExpect(jsonPath("$.result.name").exists())
                    .andExpect(jsonPath("$.result.address").exists())
                    .andExpect(jsonPath("$.result.email").exists())
                    .andExpect(jsonPath("$.result.phoneNum").exists())
                    .andExpect(jsonPath("$.result.birth").exists());
        }

        @Test
        void 회원조회_실패_아이디없음() throws Exception {

            // when
            when(userService.findUser(any()))
                    .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

            // then
            mockMvc.perform(get("/api/v1/users/1")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()));
        }
    }

    @Nested
    @DisplayName("회원정보 수정 테스트")
    class UpdateUser {

        // given
        UserResponse userResponse = UserResponse.builder()
                .userName("user")
                .password("1234")
                .name("user")
                .address("서울시")
                .email("user@gmail.com")
                .phoneNum("01012345678")
                .birth("20220919")
                .build();

        @Test
        void 회원수정_성공() throws Exception {

            // when
            when(userService.updateUser(any(), any(), any()))
                    .thenReturn(userResponse);

            // then
            mockMvc.perform(put("/api/v1/users/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userResponse)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").exists());
        }

        @Test
        void 회원수정_실패_아이디없음() throws Exception {

            // when
            when(userService.updateUser(any(), any(), any()))
                    .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

            // then
            mockMvc.perform(put("/api/v1/users/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userResponse)))
                    .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()))
                    .andDo(print());
        }

        @Test
        void 회원수정_실패_이메일중복() throws Exception {

            // when
            when(userService.updateUser(any(), any(), any()))
                    .thenThrow(new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage()));

            // then
            mockMvc.perform(put("/api/v1/users/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userResponse)))
                    .andExpect(status().is(ErrorCode.DUPLICATED_EMAIL.getHttpStatus().value()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("회원정보 삭제 테스트")
    class DeleteUser {

        // given
        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .id(1L)
                .message("회원 삭제가 완료되었습니다.")
                .build();

        @Test
        void 회원삭제_성공() throws Exception {

            // when
            when(userService.deleteUser(any(), any()))
                    .thenReturn(userDeleteDto);

            // then
            mockMvc.perform(put("/api/v1/users/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userDeleteDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").exists());
        }

        @Test
        void 회원수정_실패_유저없음() throws Exception {

            // when
            when(userService.deleteUser(any(), any()))
                    .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

            // then
            mockMvc.perform(delete("/api/v1/users/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(userDeleteDto)))
                    .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("알람 조회 테스트")
    class getAlarmsTest {

        @Test
        @WithMockUser(username = "user1")
        @DisplayName("알람 조회 성공")
        void getAlarms() throws Exception {
            // given
            UserEntity loginUser = UserEntityFixture.get("user1", "password1");
            UserEntity fromUser = UserEntityFixture.get("user2", "password2");
            RecipeEntity recipe = RecipeEntityFixture.get(loginUser);

            List<AlarmEntity> alarmEntityList = new ArrayList<>();
            alarmEntityList.add(AlarmEntityFixture.get(1L, fromUser, recipe, AlarmType.NEW_REVIEW_ON_RECIPE));
            alarmEntityList.add(AlarmEntityFixture.get(2L, fromUser, recipe, AlarmType.NEW_LIKE_ON_RECIPE));
            alarmEntityList.add(AlarmEntityFixture.get(3L, fromUser, recipe, AlarmType.NEW_LIKE_ON_RECIPE));
            Page<AlarmResponseDto> alarms = new PageImpl<>(alarmEntityList).map(AlarmResponseDto::of);

            // when
            given(alarmService.getMyAlarms(eq(loginUser.getUserName()), any(Pageable.class))).willReturn(alarms);

            // then
            mockMvc.perform(get("/api/v1/my/alarms")
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.content").isArray())
                    .andExpect(jsonPath("$.result.pageable").exists());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("알람 조회 실패 - 로그인한 유저를 찾지 못하는 경우")
        void UserNotFound() throws Exception {
            // given
            // when
            // then
            mockMvc.perform(get("/api/v1/my/alarms")
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

    }
}