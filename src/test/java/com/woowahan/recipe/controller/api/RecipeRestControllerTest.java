package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.service.RecipeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeRestController.class)
class RecipeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void 레시피_ID_단건_조회() throws Exception{
        //given
        RecipeFindResDto recipeFindResDto = RecipeFindResDto.builder()
                .recipe_id(1L)
                .recipe_title("유부초밥")
                .recipe_body("이렇게")
                .userName("bjw")
                .recipe_like(10L)
                .recipe_view(12L)
                .build();
        //when
        when(recipeService.findRecipe(any())).thenReturn(recipeFindResDto);
        //then
        mockMvc.perform(get("/api/v1/recipes/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeFindResDto)))
                .andExpect(jsonPath("$.result.recipe_id").exists())
                .andExpect(jsonPath("$.result.recipe_title").exists())
                .andExpect(jsonPath("$.result.recipe_body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.recipe_like").exists())
                .andExpect(jsonPath("$.result.recipe_view").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 레시피_등록_성공() throws Exception {
        //given
        RecipeCreateReqDto request = new RecipeCreateReqDto("hi","hello");
        //when
        when(recipeService.createRecipe(any(),any())).thenReturn(RecipeCreateResDto.builder()
                .recipe_title("hi")
                .recipe_body("hello")
                .userName("bjw")
                .build());
        //then
        mockMvc.perform(post("/api/v1/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(jsonPath("$.result.recipe_title").exists())
                .andExpect(jsonPath("$.result.recipe_body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }
}