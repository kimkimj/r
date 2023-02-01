package com.woowahan.recipe.controller.api;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    /*@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    UserEntity givenUser = UserEntity.builder()
            .id(1L)
            .userName("test")
            .address("경기도 부천시")
            .birth("20230120")
            .build();

    ItemEntity givenItem = ItemEntity.builder()
            .Id(1L)
            .itemStock(100)
            .itemPrice(1000)
            .itemName("양파")
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

        given(orderService.findOrder(givenUser.getName(), 1L))
                .willReturn(orderInfoResponse);

        mockMvc.perform(get("/api/v1/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result.orderNum").value("1111"))
//                .andExpect(jsonPath("$.result.username").value("test"))
//                .andExpect(jsonPath("$.result.address").value("경기도 부천시"))
//                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.ORDER))
//                .andExpect(jsonPath("$.result.totalPrice").value(10000))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 내주문_조회() throws Exception {
        given(orderService.findAllOrder(any(), any())).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/users/orders/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void order() throws Exception {
        OrderCreateResDto resDto = OrderCreateResDto.builder()
                .receiveUserName(givenUser.getUserName())
                .address(givenUser.getAddress())
                .orderStatus(OrderStatus.ORDER)
                .totalPrice((10 * givenItem.getItemPrice()))
                .build();

        given(orderService.createOrder(any(), any(OrderCreateReqDto.class))).willReturn(resDto);

        mockMvc.perform(post("/api/v1/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result.receiveUserName").value("test"))
//                .andExpect(jsonPath("$.result.address").value("경기도 부천시"))
//                .andExpect(jsonPath("$.result.orderStatus").value("ORDER"))
//                .andExpect(jsonPath("$.result.totalPrice").value(10000))
                .andDo(print());
    }

    @Test
    void deleteOrder() {
    }*/
}