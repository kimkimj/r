package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {


}
