package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderDeleteResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.OrderRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.woowahan.recipe.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //= 비지니스 로직 시작 =//
    public OrderCreateResDto createOrder(String username, OrderCreateReqDto reqDto) {

        // 유저 확인
        UserEntity user = validateUser(username);
        // 상품 확인
        ItemEntity item = validateItem(reqDto.getItemId());

        // 배송정보 생성
        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setAddress(user.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        // 주문 상품 생성
        OrderItemEntity orderItem = OrderItemEntity.createOrderItem(item, item.getItemPrice(), reqDto.getCount());

        // 주문 생성
        OrderEntity order = OrderEntity.createOrder(user, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return OrderCreateResDto.from(order);
    }

    @Transactional(readOnly = true)
    public OrderInfoResponse findOrder(String username, Long orderId) {
        validateUser(username);
        OrderEntity order = validateOrder(orderId);
        return OrderInfoResponse.from(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> findAllOrder(String username, Pageable pageable) {
        UserEntity user = validateUser(username);
        Page<OrderEntity> pages = orderRepository.findMyOrderByUser(user, pageable);
        return pages.map(OrderInfoResponse::from);
    }

    public OrderDeleteResDto cancelOrder(String username, Long orderId) {
        validateUser(username);
        OrderEntity order = validateOrder(orderId);
        order.cancel();
        // FIXME: 2023/01/20 soft delete 구현하기
        orderRepository.delete(order);
        return OrderDeleteResDto.from(order);
    }

    //= 비지니스 로직 종료 =//

    private OrderEntity validateOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> {
            throw new AppException(ORDER_NOT_FOUND, ORDER_NOT_FOUND.getMessage());
        });
    }

    private ItemEntity validateItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new AppException(ITEM_NOT_FOUND, ITEM_NOT_FOUND.getMessage());
        });
    }

    private UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage());
        });
    }

}
