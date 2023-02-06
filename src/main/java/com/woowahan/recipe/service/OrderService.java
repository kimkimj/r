package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.orderDto.OrderCancelResponse;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.OrderCustomRepository;
import com.woowahan.recipe.repository.OrderRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.woowahan.recipe.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCustomRepository orderCustomRepository;
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
        log.info("orderItem.getTotalPrice={}", orderItem.getTotalPrice());
        log.info("orderItem.getCount={}", orderItem.getCount());
        // 주문 생성
        OrderEntity order = OrderEntity.createOrder(user, delivery, orderItem, reqDto.getImp_uid());
        log.info("order.getTotalPrice={}", order.getTotalPrice());
        log.info("order.getTotalCounts={}", order.getTotalCounts());

        // 주문 저장
        orderRepository.save(order);
        return OrderCreateResDto.from(order);
    }

    // 장바구니에 담긴 상품 데이터를 전달받아서 주문 생성
    public OrderCreateResDto createOrderCartItem(List<OrderCreateReqDto> orderDtoList, String userName) {
        UserEntity user = validateUser(userName);
        List<OrderItemEntity> orderItemList = new ArrayList<>();

        // 주문을 위한 상품 리스트
        for (OrderCreateReqDto reqDto : orderDtoList) {
            ItemEntity itemEntity = validateItem(reqDto.getItemId());

            OrderItemEntity orderItem = OrderItemEntity.createOrderItem(itemEntity, itemEntity.getItemPrice(), reqDto.getCount());
            orderItemList.add(orderItem);
        }

        // 배송정보 생성
        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setAddress(user.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        OrderEntity order = OrderEntity.createOrder(user, delivery, orderItemList);
        orderRepository.save(order);

        return OrderCreateResDto.from(order);
    }

    @Transactional(readOnly = true)
    public OrderInfoResponse findOrderById(String userName, Long orderId) {
        validateUser(userName);
        OrderEntity order = validateOrder(orderId);
        return OrderInfoResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderInfoResponse findOrderByOrderNum(String userName, String orderNum) {
        validateUser(userName);
        OrderEntity order = orderRepository.findByOrderNumber(orderNum).orElseThrow(() -> {
            throw new AppException(ORDER_NOT_FOUND, ORDER_NOT_FOUND.getMessage());
        });
        return OrderInfoResponse.from(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> findMyOrder(String username, OrderSearch orderSearch, Pageable pageable) {
        validateUser(username);
        Page<OrderEntity> pages = orderCustomRepository.findAllByString(orderSearch, username, pageable);
        return pages.map(OrderInfoResponse::from);
    }

    public OrderCancelResponse cancelOrder(String username, Long orderId) {
        validateUser(username);
        OrderEntity order = validateOrder(orderId);
        order.cancel();

        return OrderCancelResponse.from(order);
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
