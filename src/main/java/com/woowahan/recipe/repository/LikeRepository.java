package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {  // FIXME: 2023-01-17  ID 자료형 수정
}
