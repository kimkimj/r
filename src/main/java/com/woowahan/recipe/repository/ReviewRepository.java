package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

}
