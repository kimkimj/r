package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.RecipeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeItemRepository extends JpaRepository<RecipeItemEntity,Long> {

    Optional<List<RecipeItemEntity>> findRecipeItemEntitiesByRecipe(RecipeEntity recipeEntity);
}
