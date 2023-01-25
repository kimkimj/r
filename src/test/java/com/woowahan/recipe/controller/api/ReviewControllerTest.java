package com.woowahan.recipe.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.reviewDto.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("댓글 전체 조회")
    class findAll {
        @Test
        @WithMockUser
        @DisplayName("레시피에 대한 댓글 전체 조회 성공")
        void find_all_reviews_by_recipe() throws Exception {
            mockMvc.perform(get("/api/v1/recipes/1/reviews")
                            .param("page", "0")
                            .param("size", "20")
                            .param("sort", "createdDate, desc"))
                    .andDo(print())
                    .andExpect(status().isOk());
            ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

            verify(reviewService).findAllReviews(any(), pageableArgumentCaptor.capture());
            PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

            assertEquals(Sort.by("createdDate", "desc"), pageRequest.withSort(Sort.by("createdDate", "desc")).getSort());
        }

        @Test
        @WithMockUser
        @DisplayName("특정 유저의 전체 조회 성공")
        void find_all_reviews_by_user() throws Exception {
            mockMvc.perform(get("/api/v1/recipes/reviews")
                            .param("page", "0")
                            .param("size", "20")
                            .param("sort", "createdDate, desc"))
                    .andDo(print())
                    .andExpect(status().isOk());
            ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

            verify(reviewService).findAllReviewsByUser(any(), pageableArgumentCaptor.capture());
            PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

            assertEquals(Sort.by("createdDate", "desc"), pageRequest.withSort(Sort.by("createdDate", "desc")).getSort());
        }
    }

    @Nested
    @DisplayName("댓글 등록")
    class createReview {
        @Test
        @WithMockUser
        @DisplayName("댓글 등록 성공")
        void create_review() throws Exception {
            //given
            ReviewCreateRequest request = new ReviewCreateRequest("comment1");

            //when
            when(reviewService.createReview(any(), any(), any()))
                    .thenReturn(ReviewCreateResponse.builder()
                            .reviewId(1l)
                            .comment("comment")
                            .username("username")
                            .build());
            //then

            mockMvc.perform(post("/api/v1/recipes/1/reviews")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(jsonPath("$.result.reviewId").exists())
                    .andExpect(jsonPath("$.result.username").exists())
                    .andExpect(jsonPath("$.result.comment").exists())
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("댓글 등록 실패 - 로그인 하지 않은 경우")
        void create_review_fail_1() throws Exception {

            //when
            ReviewCreateRequest request = new ReviewCreateRequest("comment1");

            given(reviewService.createReview(any(), any(), any()))
                    .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));
            //then

            mockMvc.perform(post("/api/v1/recipes/1/reviews")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    class updateReview {
        @Test
        @WithMockUser
        @DisplayName("댓글 수정 성공")
        void update_review() throws Exception {
            //given
            ReviewCreateRequest request = new ReviewCreateRequest("comment edited");

            //when
            when(reviewService.updateReview(any(), any(), any(), any()))
                    .thenReturn(ReviewUpdateResponse.builder()
                            .reviewId(1l)
                            .message("리뷰가 수정되었습니다")
                            .build());
            //then

            mockMvc.perform(put("/api/v1/recipes/1/reviews/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(jsonPath("$.result.reviewId").exists())
                    .andExpect(jsonPath("$.result.message").exists())
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("댓글 수정 실패 - 유저가 일치 하지 않는 경우")
        void update_review_fail() throws Exception {
            //when
            ReviewCreateRequest request = new ReviewCreateRequest("comment edited");
            given(reviewService.updateReview(any(), any(), any(), any()))
                    .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));
            //then

            mockMvc.perform(put("/api/v1/recipes/1/reviews/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("댓글 삭제")
    class deleteReview {
        @Test
        @WithMockUser
        @DisplayName("댓글 삭제 성공")
        void delete_review() throws Exception {

            //given
            ReviewDeleteResponse response = new ReviewDeleteResponse(1l, "댓글이 삭제되었습니다");
            when(reviewService.deleteReview(any(), any(), any())).thenReturn(response);

            //then
            mockMvc.perform(delete("/api/v1/recipes/1/reviews/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.result.reviewId").exists())
                    .andExpect(jsonPath("$.result.message").exists())
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("댓글 삭제 실패 - 작성자와 일치하지 않는 경우")
        void create_review_fail_1() throws Exception {
            given(reviewService.deleteReview(any(), any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

            mockMvc.perform(delete("/api/v1/recipes/1/reviews/1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }


    }

}