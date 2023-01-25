package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderDeleteResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.exception.NotEnoughStockException;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.OrderRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserEntity user = Mockito.mock(UserEntity.class);
    ItemEntity item = Mockito.mock(ItemEntity.class);
    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, userRepository, itemRepository);
    }

    // given
    UserEntity givenUser = UserEntity.builder()
            .userName("test")
            .name("test")
            .build();

    ItemEntity givenItem = ItemEntity.builder()
            .id(1L)
            .itemName("양파")
            .itemPrice(1000)
            .itemStock(10)
            .build();

    DeliveryEntity delivery = new DeliveryEntity(givenUser.getAddress(), DeliveryStatus.READY);

    // FIXME: 2023/01/19 허진혁 주문 테스트 의문 -> 의논 필요
    @Test
    void 상품주문() throws Exception {
        // given
        OrderCreateReqDto reqDto = new OrderCreateReqDto(1L, 4);
        OrderItemEntity givenOrderItem = OrderItemEntity.createOrderItem(givenItem, givenItem.getItemPrice(), reqDto.getCount());
        OrderEntity givenOrder = OrderEntity.createOrder(givenUser, delivery, givenOrderItem);
        when(userRepository.findByUserName(givenUser.getUserName())).thenReturn(Optional.of(givenUser));
        when(itemRepository.findById(givenItem.getId())).thenReturn(Optional.of(givenItem));
        when(orderRepository.findById(givenOrderItem.getId())).thenReturn(Optional.of(givenOrder));

        // when
        OrderCreateResDto order = orderService.createOrder(givenUser.getUserName(), reqDto);

        // then
        assertEquals(OrderStatus.ORDER, order.getOrderStatus()); // 주문 상태
        assertEquals(4, order.getTotalCount()); // 주문 수량
        assertEquals(4000, order.getTotalPrice()); // 주문 가격
        assertEquals(2, givenItem.getItemStock()); // 재고 수량 확인
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {
        // given
        OrderCreateReqDto reqDto = new OrderCreateReqDto(1L, 20);
        // when
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () -> {
            OrderItemEntity.createOrderItem(givenItem, givenItem.getItemPrice(), reqDto.getCount());
        });
        // then
        assertEquals(ErrorCode.NOT_ENOUGH_STOCK.getMessage(), notEnoughStockException.getMessage());
    }

    // FIXME: 2023/01/19 허진혁 - 수정해야함
    @Test
    void 주문취소() throws Exception {
        // given
        OrderItemEntity givenOrderItem = OrderItemEntity.createOrderItem(givenItem, givenItem.getItemPrice(), 1);
        OrderEntity givenOrder = OrderEntity.createOrder(givenUser, delivery, givenOrderItem);

        when(userRepository.findByUserName(givenUser.getUserName())).thenReturn(Optional.of(givenUser));
        when(itemRepository.findById(givenItem.getId())).thenReturn(Optional.of(givenItem));
        when(orderRepository.findById(givenOrderItem.getId())).thenReturn(Optional.of(givenOrder));
        // when
        OrderDeleteResDto orderDeleteResDto = orderService.cancelOrder(givenUser.getName(), givenItem.getId());
        // then
        assertEquals(OrderStatus.CANCEL, orderDeleteResDto.getOrderStatus());
        assertEquals(9, givenItem.getItemStock());
    }


}