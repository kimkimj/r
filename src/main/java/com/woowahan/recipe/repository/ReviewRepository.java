package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Page<ReviewEntity> findAllByRecipe(RecipeEntity recipe, Pageable pageable);

    @Modifying
    @Query("update ReviewEntity a set a.deletedDate = CURRENT_DATE where a.reviewId = :reviewId")
    void deleteReviewEntityByReviewId(@Param("reviewId") Long reviewId);
}
