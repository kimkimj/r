package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
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

        /* given */
        ItemCreateReqDto itemCreateReqDto = new ItemCreateReqDto("imagepath", "name", 1000, 5);
        given(itemService.createItem(any(ItemCreateReqDto.class), any()))
                .willReturn(new ItemCreateResDto(1L, "name", "상품 등록 완료"));
        /* when */
        ResultActions resultActions = mockMvc.perform(post("/api/v1/items")
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsBytes(itemCreateReqDto)))
                                        .andDo(print());
        /* then */
        resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                        .andExpect(jsonPath("$.result.itemName").value("name"))
                        .andExpect(jsonPath("$.result.message").value("상품 등록 완료"));
    }

    @Test
    @WithAnonymousUser
    void 아이템_등록_실패_권한없음() throws Exception {

        /* given */
        ItemCreateReqDto itemCreateReqDto = new ItemCreateReqDto("imagepath", "name", 1000, 5);
        given(itemService.createItem(any(ItemCreateReqDto.class), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));
        /* when */
        ResultActions resultActions = mockMvc.perform(post("/api/v1/items")
                                        .with(csrf())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsBytes(itemCreateReqDto)))
                                        .andDo(print());
        /* then */
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 아이템_수정_성공() throws Exception {

        /* given */
        ItemUpdateReqDto itemUpdateReqDto = new ItemUpdateReqDto("imagepath", "name", 1000, 5);
        given(itemService.updateItem(any(), any(ItemUpdateReqDto.class), any()))
                .willReturn(new ItemUpdateResDto(1L, "imagepath", "name", 1000, 5, "상품 수정 완료"));
        /* when */
        ResultActions resultActions = mockMvc.perform(put("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(itemUpdateReqDto)))
                        .andDo(print());
        /* then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.itemImagePath").value("imagepath"))
                .andExpect(jsonPath("$.result.itemName").value("name"))
                .andExpect(jsonPath("$.result.itemPrice").value(1000))
                .andExpect(jsonPath("$.result.itemStock").value(5))
                .andExpect(jsonPath("$.result.message").value("상품 수정 완료"));
    }

    @Test
    @WithAnonymousUser
    void 아이템_수정_실패_권한없음() throws Exception {

        /* given */
        ItemUpdateReqDto itemUpdateReqDto = new ItemUpdateReqDto("imagepath", "name", 1000, 5);
        given(itemService.updateItem(any(), any(ItemUpdateReqDto.class), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));
        /* when */
        ResultActions resultActions = mockMvc.perform(put("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(itemUpdateReqDto)))
                        .andDo(print());
        /* then */
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 아이템_삭제_성공() throws Exception {

        /* given */
        given(itemService.deleteItem(any(), any()))
                .willReturn(new ItemDeleteResDto(1L, "상품 삭제 완료"));
        /* when */
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/items/1")
                                            .with(csrf())
                                            .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print());
        /* then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("상품 삭제 완료"));
    }

    @Test
    @WithAnonymousUser
    void 아이템_삭제_실패_권한없음() throws Exception {

        /* when */
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
        /* then */
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 아이템_전체조회_성공() throws Exception {

        /* when */
        mockMvc.perform(get("/api/v1/items")
                        .param("page", "0")
                        .param("size", "50")
                        .param("sort", "createdDate,desc"))
                        .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(itemService).findAllItem(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        /* then */
        assertEquals(0, pageable.getPageNumber());
        assertEquals(50, pageable.getPageSize());
        assertEquals(Sort.by("createdDate", "desc"), pageable.withSort(Sort.by("createdDate", "desc")).getSort());
    }

    @Test
    @WithMockUser
    void 아이템_상세조회_성공() throws Exception {

        /* given */
        ItemDetailResDto dto = ItemDetailResDto.builder()
                .itemImagePath("image path")
                .itemName("name")
                .itemPrice(3000)
                .itemStock(10)
                .build();
        given(itemService.findItem(any())).willReturn(dto);
        /* when */
        ResultActions resultActions = mockMvc.perform(get("/api/v1/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        /* then */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.itemImagePath").value("image path"))
                .andExpect(jsonPath("$.result.itemName").value("name"))
                .andExpect(jsonPath("$.result.itemPrice").value(3000))
                .andExpect(jsonPath("$.result.itemStock").value(10));
    }

}