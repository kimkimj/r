package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeServiceTest {

    RecipeService recipeService;

    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    /**
     * 유저엔티티 생성
     */
    private final Long user_id = 15l;
    private final String nickanme = "백종원";
    private final UserEntity userEntity = UserEntity.builder()
            .userId(user_id)
            .nickname(nickanme)
            .build();

    /**
     * 레시피엔티티 생성
     */
    private final Long id = 1l;
    private final String title = "유부초밥";
    private final String body = "이렇게";
    private final Long like = 10l;
    private final Long view = 12l;
    private final RecipeEntity recipeEntity = RecipeEntity.builder()
            .recipe_id(id)
            .recipe_title(title)
            .recipe_body(body)
            .user_id(userEntity)
            .recipe_like(like)
            .recipe_view(view)
            .build();

    @BeforeEach
    void beforEach() {
        recipeService = new RecipeService(recipeRepository,userRepository);
    }

    @Test
    void 레시피_ID_단건_조회() {

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        assertThat(recipeFindResDto.getRecipe_title()).isEqualTo("유부초밥");
        assertThat(recipeFindResDto.getUser_nickname()).isEqualTo("백종원");
    }
}