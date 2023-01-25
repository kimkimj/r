package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.LikeEntity;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.fixture.LikeEntityFixture;
import com.woowahan.recipe.fixture.RecipeEntityFixture;
import com.woowahan.recipe.fixture.UserEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("좋아요 개수 세기 테스트")
    void countLikesTest() {
        // given
        UserEntity userEntity = UserEntityFixture.get("testUser", "testPassword");
        RecipeEntity recipeEntity = RecipeEntityFixture.get(userEntity);
        LikeEntity likeEntity = LikeEntityFixture.createLike(userEntity, recipeEntity);
        likeRepository.save(likeEntity);

        // when
        Integer likeCnt = likeRepository.countByRecipe(recipeEntity);

        // then
        assertEquals(likeCnt, 1);
    }
}