package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    /**
     * 조회수 증가 쿼리
     */
    @Modifying
    @Query("update RecipeEntity r set r.recipe_view = r.recipe_view + 1 where r.id = :id")
    int updateView(@Param("id") Long id);

    /**
     * 레시피 마이피드
     */
    Page<RecipeEntity> findRecipeEntitiesByUserName(String userName, Pageable pageable);
    Optional<RecipeEntity> findByUserName(String userName);
}
