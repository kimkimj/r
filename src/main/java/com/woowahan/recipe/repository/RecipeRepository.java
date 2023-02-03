package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.RecipeItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    /**
     * 조회수 증가 쿼리
     */
    @Modifying
    @Query("update RecipeEntity r set r.recipeView = r.recipeView + 1 where r.id = :id")
    int updateView(@Param("id") Long id);

    // 레시피 마이피드
    /**
     * 좋아요 개수 증감 쿼리
     */
    @Modifying
    @Query("update RecipeEntity r set r.recipeLike = r.recipeLike + 1 where r.id = :id")
    Integer increaseLikeCounts(@Param("id") Long id);

    @Modifying
    @Query("update RecipeEntity r set r.recipeLike = r.recipeLike - 1 where r.id = :id")
    Integer decreaseLikeCounts(@Param("id") Long id);

    /**
     * 레시피 마이피드
     */
    Page<RecipeEntity> findRecipeEntitiesByUser(UserEntity user, Pageable pageable);
    Optional<List<RecipeEntity>> findByUser(UserEntity user);


//    // 레시피 검색
//    @Query(value = "SELECT re FROM RecipeEntity re WHERE re.recipeTitle LIKE %:recipeTitle%",
//           countQuery = "SELECT COUNT(re.id) FROM RecipeEntity re WHERE re.recipeTitle LIKE %:recipeTitle%")
//    Page<RecipeEntity> findAllSearch(String recipeTitle, Pageable pageable);

    Page<RecipeEntity> findByRecipeTitleContaining(String recipeTitle, Pageable pageable);

}
