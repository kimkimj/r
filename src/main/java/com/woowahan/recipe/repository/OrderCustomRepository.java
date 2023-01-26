package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCustomRepository{
    // 상품 주문 내역 조회
    List<OrderEntity> findAllByString(OrderSearch orderSearch);
}
