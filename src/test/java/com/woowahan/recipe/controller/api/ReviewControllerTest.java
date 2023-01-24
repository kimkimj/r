package com.woowahan.recipe.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateRequest;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewCreateResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewDeleteResponse;
import com.woowahan.recipe.domain.dto.reviewDto.ReviewListResponse;
import com.woowahan.recipe.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
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

    /*
    @Test
    @WithMockUser
    void find_all_reviews() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("reviewId").descending());


        //given
        ReviewListResponse response = new ReviewListResponse(any(), any());

        //when
        when(reviewService.createReview(any(), any(), any()))
                .thenReturn(ReviewCreateResponse.builder()
                        .reivew_id(1l)
                        .comment("comment")
                        .username("username")
                        .build());
        //then

        mockMvc.perform(get("/api/v1/recipes/1/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(response)))
                .andExpect(jsonPath("$.result.reivew_id").exists())
                .andExpect(jsonPath("$.result.username").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }*/


    @Test
    @WithMockUser
    void create_review() throws Exception {
        //given
        ReviewCreateRequest request = new ReviewCreateRequest("comment1");

        //when
        when(reviewService.createReview(any(), any(), any()))
                .thenReturn(ReviewCreateResponse.builder()
                                                .reivew_id(1l)
                                                .comment("comment")
                                                .username("username")
                                                .build());
        //then

        mockMvc.perform(post("/api/v1/recipes/1/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.result.reivew_id").exists())
                .andExpect(jsonPath("$.result.username").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 성공")
    void update_review() throws Exception {
        //given
        ReviewCreateRequest request = new ReviewCreateRequest("comment edited");

        //when
        when(reviewService.updateReview(any(), any(), any(), any()))
                .thenReturn(ReviewCreateResponse.builder()
                        .reivew_id(1l)
                        .comment("comment edited")
                        .username("username")
                        .build());
        //then

        mockMvc.perform(put("/api/v1/recipes/1/reviews/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.result.reivew_id").exists())
                .andExpect(jsonPath("$.result.username").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 성공")
    void delete_review() throws Exception {
        //given
        ReviewDeleteResponse response = new ReviewDeleteResponse(1l, "댓글이 삭제되었습니다");

        //when
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



}