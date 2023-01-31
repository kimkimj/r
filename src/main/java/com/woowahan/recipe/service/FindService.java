package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.RecipeCustomRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class FindService {

    private final UserRepository userRepository;
    private final RecipeCustomRepository recipeCustomRepository;

    public UserEntity findUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
        });
    }

    public List<RecipeFindResDto> findMyLikeRecipe(String userName) {
        List<RecipeEntity> myLikeRecipeList = recipeCustomRepository.findByUserAndLove(userName);
        return myLikeRecipeList.stream().map(RecipeEntity::from).collect(Collectors.toList());
    }

}
