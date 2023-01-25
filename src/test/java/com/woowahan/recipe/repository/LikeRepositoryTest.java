package com.woowahan.recipe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;


    // TODO: 2023-01-25 h2 데이터베이스 연결 repository test 도전중
    /*@Test
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
    }*/
}