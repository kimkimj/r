package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @MockBean
    PaymentService paymentService;

    UserEntity givenUser = UserEntity.builder()
            .id(1L)
            .userName("test")
            .address("경기도 부천시")
            .birth("20230120")
            .build();

    ItemEntity givenItem = ItemEntity.builder()
            .id(1L)
            .itemStock(100)
            .itemPrice(1000)
            .name("양파")
            .build();

    @Test
    @WithMockUser
    void 주문_단건조회() throws Exception {

        OrderInfoResponse orderInfoResponse = OrderInfoResponse.builder()
                .orderNum("1111")
                .username(givenUser.getUserName())
                .address(givenUser.getAddress())
                .orderStatus(OrderStatus.ORDER)
                .totalPrice(10000)
                .build();

        given(orderService.findOrderById(any(), any()))
                .willReturn(orderInfoResponse);

        mockMvc.perform(get("/api/v1/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.orderNum").value("1111"))
                .andExpect(jsonPath("$.result.username").value("test"))
                .andExpect(jsonPath("$.result.address").value("경기도 부천시"))
                .andExpect(jsonPath("$.result.orderStatus").value("ORDER"))
                .andExpect(jsonPath("$.result.totalPrice").value(10000))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 내주문_조회() throws Exception {
        given(orderService.findMyOrder(any(), any(), any())).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/orders/users/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 주문() throws Exception {
        OrderCreateReqDto reqDto = OrderCreateReqDto.builder()
                .itemId(givenItem.getId())
                .count(10)
                .build();
        OrderCreateResDto resDto = OrderCreateResDto.builder()
                .receiveUserName(givenUser.getUserName())
                .address(givenUser.getAddress())
                .orderStatus(OrderStatus.ORDER)
                .totalPrice((10 * givenItem.getItemPrice()))
                .build();

        given(orderService.createOrder(any(), any(OrderCreateReqDto.class))).willReturn(resDto);

        mockMvc.perform(post("/api/v1/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.receiveUserName").value("test"))
                .andExpect(jsonPath("$.result.address").value("경기도 부천시"))
                .andExpect(jsonPath("$.result.orderStatus").value("ORDER"))
                .andExpect(jsonPath("$.result.totalPrice").value(10000))
                .andDo(print());
    }
}