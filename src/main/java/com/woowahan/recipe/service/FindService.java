package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.RecipeCustomRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<RecipeFindResDto> findMyLikeRecipe(String userName, Pageable pageable) {
        Page<RecipeEntity> pages = recipeCustomRepository.findByUserAndLove(userName, pageable);
        return pages.map(RecipeEntity::from);
    }

}
