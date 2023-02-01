package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCustomRepository{
    // 상품 주문 내역 조회
    Page<OrderEntity> findAllByString(OrderSearch orderSearch, String userName, Pageable pageable);
}
