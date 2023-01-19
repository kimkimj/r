package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findByItemNameContaining(String keyword, Pageable pageable);

}
