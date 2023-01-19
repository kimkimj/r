package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeServiceTest {

    RecipeService recipeService;

    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    /**
     * 유저엔티티 생성
     */
    private final Long user_id = 15L;
    private final String userName = "bjw";
    private final UserEntity userEntity = UserEntity.builder()
            .id(user_id)
            .userName(userName)
            .build();

    /**
     * 레시피엔티티 생성
     */
    private final Long id = 1L;
    private final String title = "유부초밥";
    private final String body = "이렇게";
    private final Long like = 10L;
    private final Long view = 12L;
    private final RecipeEntity recipeEntity = RecipeEntity.builder()
            .id(id)
            .recipe_title(title)
            .recipe_body(body)
            .user(userEntity)
            .recipe_like(like)
            .recipe_view(view)
            .build();

    @BeforeEach
    void beforeEach() {
        recipeService = new RecipeService(recipeRepository,userRepository);
    }

    @Test
    void 레시피_ID_단건_조회() {

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        assertThat(recipeFindResDto.getRecipe_title()).isEqualTo("유부초밥");
        assertThat(recipeFindResDto.getUserName()).isEqualTo("bjw");
    }

    @Test
    @WithMockUser
    void 레시피_등록_성공() {

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(recipeRepository.save(any())).thenReturn(recipeEntity);

        Assertions.assertDoesNotThrow(
                () -> recipeService.createRecipe(new RecipeCreateReqDto(
                        title,body),userName));
    }

}