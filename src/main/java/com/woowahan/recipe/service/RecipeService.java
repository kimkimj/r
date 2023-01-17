package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    /**
     * ID로 레시피 단건조회
     * @param recipe_id
     * @return recipeFindResDto
     */
    public RecipeFindResDto findRecipe(Long recipe_id) {
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(recipe_id);
        RecipeFindResDto recipeFindResDto = RecipeEntity.from(optRecipe.get());
        return recipeFindResDto;
    }


}
