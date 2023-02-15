package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.service.CartService;
import com.woowahan.recipe.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartRestController.class)
@WithMockUser(username = "testUser")
class CartRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @MockBean
    PaymentService paymentService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("장바구니 아이템 등록 테스트")
    class createCartItemTest {

        @Test
        @DisplayName("로그인하지 않은 회원이 접근한 경우")
        @WithAnonymousUser
        void notLogin() throws Exception {
            mockMvc.perform(post("/api/v1/carts")
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

//        @Test
//        @DisplayName("장바구니 등록 성공")
//        void createCartItem() throws Exception {
//            String userName = TestInfoFixture.get().getUserName();
//            Long itemId = TestInfoFixture.get().getItemId();
//            CartItemReq cartItem = CartItemReq.builder()
//                                                        .itemId(itemId)
//                                                        .itemCnt(3)
//                                                        .build();
//
//            doNothing().when(cartService).createCartItem(cartItem, userName);
//
//            mockMvc.perform(post("/api/v1/carts")
//                            .with(csrf()))
//                            .andExpect(status().isOk())
//                            .andExpect(jsonPath("$.result").value("상품이 장바구니에 담겼습니다."));
//        }
    }



}