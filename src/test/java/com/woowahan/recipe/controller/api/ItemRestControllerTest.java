package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateResDto;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ItemRestControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser
    void 아이템_등록_성공() throws Exception {
        ItemCreateReqDto itemCreateReqDto = new ItemCreateReqDto("imagepath", "name", 1000, 5);

        when(itemService.createItem(any(ItemCreateReqDto.class), any()))
                .thenReturn(new ItemCreateResDto("name", "상품 등록 완료"));

        mockMvc.perform(post("/api/v1/items")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(itemCreateReqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                        .andExpect(jsonPath("$.result.itemName").value("name"))
                        .andExpect(jsonPath("$.result.message").value("상품 등록 완료"));
    }

    @Test
    @WithAnonymousUser
    void 아이템_등록_실패_권한없음() throws Exception {
        ItemCreateReqDto itemCreateReqDto = new ItemCreateReqDto("imagepath", "name", 1000, 5);

        when(itemService.createItem(any(ItemCreateReqDto.class), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/items")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(itemCreateReqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
    }




}