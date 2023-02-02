package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    ItemEntity findByName(String name);

    Page<ItemEntity> findByNameContaining(String keyword, Pageable pageable);

    // 레시피 검색
    @Query(value = "SELECT it FROM ItemEntity it WHERE it.name LIKE %:name%",
            countQuery = "SELECT COUNT(it.id) FROM ItemEntity it WHERE it.name LIKE %:name%")
    Page<ItemEntity> findAllSearch(String name, Pageable pageable);
}
