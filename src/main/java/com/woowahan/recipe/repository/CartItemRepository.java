package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.CartItemEntity;
import com.woowahan.recipe.domain.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Page<CartItemEntity> findByCart(CartEntity cart, Pageable pageable);

    @Query("select i from CartItemEntity i where i.cart = :cart and i.item.id = :itemId")
    Optional<CartItemEntity> findByCartAndItemId(@Param("cart") CartEntity cart, @Param("itemId") Long itemId);

    @Query("select i from CartItemEntity i where i.cart = :cart and i.id = :cartItemId")
    Optional<CartItemEntity> findByCartAndId(@Param("cart") CartEntity cart, @Param("cartItemId") Long id);

    @Query("select i from CartItemEntity i where i.id = :id and i.isChecked = true")
    Optional<CartItemEntity> findByIdAndChecked(@Param("id") Long id);

    Optional<CartItemEntity> findByItem(ItemEntity item);

    void deleteByItem(ItemEntity item);
}
