package com.woowahan.recipe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.woowahan.recipe.domain.entity.QDeliveryEntity.deliveryEntity;
import static com.woowahan.recipe.domain.entity.QItemEntity.itemEntity;
import static com.woowahan.recipe.domain.entity.QOrderEntity.orderEntity;
import static com.woowahan.recipe.domain.entity.QOrderItemEntity.orderItemEntity;
import static com.woowahan.recipe.domain.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory queryFactory;

    // 상품 주문 내역 조회
    // 주문 진행, 주문 취소 조건 필요
    // 상품 이름에 대한 조건 필요
    @Override
    public Page<OrderEntity> findAllByString(OrderSearch orderSearch, String userName, Pageable pageable) {
        return searchOrder(orderSearch.getOrderStatus(), orderSearch.getItemName(), userName, pageable);
    }

    private Page<OrderEntity> searchOrder(OrderStatus orderStatusCond, String itemNameCond, String userNameCond, Pageable pageable) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (userNameCond != null) {
            booleanBuilder.and(userEntity.userName.eq(userNameCond));
        }

        if (orderStatusCond != null) {
            booleanBuilder.and(orderEntity.orderStatus.eq(orderStatusCond));
        }

        if (itemNameCond != null) {
            booleanBuilder.and(itemEntity.name.contains(itemNameCond));
        }
        List<OrderEntity> contents = queryFactory.select(orderEntity)
                .from(orderEntity)
                .join(orderEntity.user, userEntity).fetchJoin()
                .join(orderEntity.delivery, deliveryEntity).fetchJoin()
                .join(orderEntity.orderItems, orderItemEntity).fetchJoin()
                .join(orderItemEntity.item, itemEntity).fetchJoin()
                .where(booleanBuilder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = contents.size();
        return new PageImpl<>(contents, pageable, total);
    }
}
