package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.OrderCreateResDto;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.OrderRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderCreateResDto createOrder(String userName, Long itemId, int count) {

        UserEntity user = validateUser(userName);
        ItemEntity item = validateItem(itemId);

        DeliveryEntity delivery = new DeliveryEntity();
        delivery.addDelivery(user);
        delivery.changeStatus(DeliveryStatus.READY);

        OrderItemEntity orderItem = OrderItemEntity.createOrderItem(item, item.getItemPrice(), count);

        OrderEntity order = OrderEntity.createOrder(user, delivery, orderItem);

        orderRepository.save(order);
        return OrderCreateResDto.from(order);
    }

    private ItemEntity validateItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new RuntimeException("재료가 존재하지 않습니다.");
        });
    }

    private UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new RuntimeException("유저 네임 존재하지 않습니다.");
        });
    }

}
