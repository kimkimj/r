package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.alarmDto.AlarmResponseDto;
import com.woowahan.recipe.domain.entity.AlarmEntity;
import com.woowahan.recipe.domain.entity.AlarmType;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.fixture.AlarmEntityFixture;
import com.woowahan.recipe.fixture.RecipeEntityFixture;
import com.woowahan.recipe.fixture.UserEntityFixture;
import com.woowahan.recipe.service.AlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmRestController.class)
@WithMockUser
class AlarmRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    @Autowired
    ObjectMapper objectMapper;

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
            mockMvc.perform(get("/api/v1/alarms")
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
            mockMvc.perform(get("/api/v1/alarms")
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

    }

}