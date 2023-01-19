package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findAllByRecipe(RecipeEntity recipe);
    @Modifying
    @Query("update ReviewEntity a set a.deletedDate = CURRENT_DATE where a.reviewId = :reviewId")
    void deleteReviewEntityByReviewId(@Param("reviewId") Long reviewId);
}
