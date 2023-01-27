package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.CartItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Page<CartItemEntity> findByCart(Pageable pageable, CartEntity cart);

    Optional<CartItemEntity> findByIdAndCart(Long itemId, CartEntity cart);
}
