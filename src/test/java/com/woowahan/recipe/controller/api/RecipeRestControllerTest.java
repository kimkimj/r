package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.fixture.TestInfoFixture;
import com.woowahan.recipe.service.RecipeService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeRestController.class)
@WithMockUser(username = "testUser")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)  // 언더로 표시한 부분 공백 표시
class RecipeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 레시피_ID_단건_조회() throws Exception {
        //given
        RecipeFindResDto recipeFindResDto = RecipeFindResDto.builder()
                .recipeId(1L)
                .recipeTitle("유부초밥")
                .recipeBody("이렇게")
                .userName("bjw")
                .recipeLike(10)
                .recipeView(12)
                .build();
        //when
        when(recipeService.findRecipe(any())).thenReturn(recipeFindResDto);
        //then
        mockMvc.perform(get("/api/v1/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipeFindResDto)))
                .andExpect(jsonPath("$.result.recipeId").exists())
                .andExpect(jsonPath("$.result.recipeTitle").exists())
                .andExpect(jsonPath("$.result.recipeBody").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.recipeLike").exists())
                .andExpect(jsonPath("$.result.recipeView").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 레시피_전체_조회_성공() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/v1/recipes")
                        .param("size", "20")
                        .param("sort", "createdDate, DESC"))
                .andExpect(status().isOk());
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(recipeService).findAllRecipes(pageableArgumentCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertThat(pageRequest.withSort(Sort.by("createdDate", "DESC")).getSort()).isEqualTo(Sort.by("createdAt", "DESC"));
        assertThat(pageRequest.getPageSize()).isEqualTo(20);
    }

    @Test
    void 레시피_등록_성공() throws Exception {
        //given
        RecipeCreateReqDto request = new RecipeCreateReqDto("hi", "hello");
        //when
        when(recipeService.createRecipe(any(), any())).thenReturn(RecipeCreateResDto.builder()
                .recipeId(1L)
                .recipeTitle("hi")
                .recipeBody("hello")
                .userName("bjw")
                .createdDate((LocalDateTime.now()))
                .build());
        //then
        mockMvc.perform(post("/api/v1/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 레시피_수정_성공() throws Exception {
        //given
        RecipeUpdateReqDto recipeUpdateReqDto = new RecipeUpdateReqDto("수정제목", "수정내용");
        //when
        when(recipeService.updateRecipe(any(), any(), any())).thenReturn(RecipeUpdateResDto.builder()
                .recipeId(1L)
                .recipeTitle("수정제목")
                .recipeBody("수정내용")
                .userName("bjw")
                .localDateTime(LocalDateTime.now())
                .build());
        //then
        mockMvc.perform(put("/api/v1/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipeUpdateReqDto)))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 레시피_삭제_성공() throws Exception {
        //given
        RecipeResponse recipeResponse = new RecipeResponse("레시피를 삭제했습니다.", 1L);
        //when
        when(recipeService.deleteRecipe(any(), any())).thenReturn(recipeResponse);

        //then
        mockMvc.perform(delete("/api/v1/recipes/1")
                        .with(csrf()))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Nested
    @DisplayName("좋아요 테스트")
    class LikeTest {

        @Test
        @DisplayName("좋아요 누르기 테스트")
        void pushLikeTest() throws Exception {
            // given
            Long recipeId = TestInfoFixture.get().getRecipeId();
            String userName = TestInfoFixture.get().getUserName();
            String pushLikesMessage = "좋아요를 눌렀습니다.";

            // when
            given(recipeService.pushLikes(recipeId, userName)).willReturn(pushLikesMessage);

            // then
            mockMvc.perform(post(String.format("/api/v1/recipes/{id}/likes"), recipeId)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."));
        }

        @Test
        @DisplayName("좋아요 취소 테스트")
        void cancelLikeTest() throws Exception {
            // given
            Long recipeId = TestInfoFixture.get().getRecipeId();
            String userName = TestInfoFixture.get().getUserName();

            // when
            given(recipeService.pushLikes(recipeId, userName)).willReturn("좋아요를 취소합니다.");

            // then
            mockMvc.perform(post(String.format("/api/v1/recipes/{id}/likes"), recipeId)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result").value("좋아요를 취소합니다."));
        }
    }
}