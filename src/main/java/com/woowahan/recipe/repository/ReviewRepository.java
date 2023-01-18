package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Modifying
    @Query("update ReviewEntity a set a.deletedDate = CURRENT_DATE where a.reviewId = :reviewId")
    void deleteReviewEntityByReviewId(@Param("reviewId") Long reviewId);
}
