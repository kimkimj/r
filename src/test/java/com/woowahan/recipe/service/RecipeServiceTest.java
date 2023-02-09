package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeUpdateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeUpdateResDto;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.fixture.LikeEntityFixture;
import com.woowahan.recipe.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WithMockUser
class RecipeServiceTest {

    RecipeService recipeService;

    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    LikeRepository likeRepository = mock(LikeRepository.class);
    ItemRepository itemRepository = mock(ItemRepository.class);
    RecipeItemRepository recipeItemRepository = mock(RecipeItemRepository.class);
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    S3Uploader s3Uploader = mock(S3Uploader.class);

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
     * 다른 유저
     */
    private final Long user_id2 = 20L;
    private final String userName2 = "kp";
    private final UserEntity userEntity2 = UserEntity.builder()
            .id(user_id2)
            .userName(userName2)
            .build();

    /**
     * 레시피엔티티 생성
     */
    private final Long id = 1L;
    private final String title = "유부초밥";
    private final String body = "이렇게";
    private final List<String> items = new ArrayList<>();

    private final int like = 10;
    private final int view = 12;

    /**
     * RecipeItemEntity를 먼저 생성
     */
    private final RecipeItemEntity recipeItemEntity = RecipeItemEntity.builder()
            .item(new ItemEntity())
//            .recipe(new RecipeEntity())
            .build();

    // List 형태의 RecipeItemEntity 선언
    List<RecipeItemEntity> recipeItemEntityList = new ArrayList<>();

    /**
     * RecipeEntity 생성
     */
    private final RecipeEntity recipeEntity = RecipeEntity.builder()
            .id(id)
            .recipeTitle(title)
            .recipeBody(body)
            .user(userEntity)
            .recipeLike(like)
            .recipeView(view)
            .items(recipeItemEntityList)
            .build();

    /**
     * ItemEntity에 아이디값과 네임값을 주기 위해 생성
     */
    private final ItemEntity itemEntity1 = ItemEntity.builder()
            .id(1L)
            .name("파")
            .build();

    private final ItemEntity itemEntity2 = ItemEntity.builder()
            .id(2L)
            .name("양파")
            .build();
    /**
     * RecipeItemEntity 생성
     */
    private final RecipeItemEntity recipeItemEntity1 = RecipeItemEntity.builder()
            .item(itemEntity1)
            .recipe(recipeEntity)
            .build();
    private final RecipeItemEntity recipeItemEntity2 = RecipeItemEntity.builder()
            .item(itemEntity2)
            .recipe(recipeEntity)
            .build();

    @BeforeEach
    void beforeEach() {
        recipeService = new RecipeService(recipeRepository, userRepository, likeRepository, itemRepository, recipeItemRepository, publisher, s3Uploader);
        items.add(0,"파");
        items.add(1,"양파");
        recipeItemEntityList.add(0,recipeItemEntity1);
        recipeItemEntityList.add(1,recipeItemEntity2);
    }

    @Test
    void 레시피_ID_단건_조회_성공() {
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        when(recipeItemRepository.findRecipeItemEntitiesByRecipe(recipeEntity)).thenReturn(Optional.of(recipeItemEntityList));
        RecipeFindResDto recipeFindResDto = recipeService.findRecipe(id);
        assertThat(recipeFindResDto.getRecipeTitle()).isEqualTo("유부초밥");
        assertThat(recipeFindResDto.getUserName()).isEqualTo("bjw");
        assertThat(recipeFindResDto.getItems().get(0).getItem().getId()).isEqualTo(1);
        assertThat(recipeFindResDto.getItems().get(1).getItem().getName()).isEqualTo("양파");
    }

    @Test
    void 레시피_마이피드_실패_유저가_DB에없는경우() {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("createdDate"));
        String userName3 = "messi";
        AppException appException = assertThrows(AppException.class, () -> recipeService.myRecipes(pageRequest, userName3));
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCode.USERNAME_NOT_FOUND);
    }

    @Test
    void 레시피_등록_성공() {

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(recipeRepository.save(any())).thenReturn(recipeEntity);
        when(recipeItemRepository.save(any())).thenReturn(recipeItemEntity);
//        assertDoesNotThrow(
//                () -> recipeService.createRecipe(new RecipeCreateReqDto(
//                        title, body, items), userName));
    }

    @Test
    void 레시피_수정_성공() {

        RecipeUpdateReqDto recipeUpdateReqDto = new RecipeUpdateReqDto("수정제목", "수정내용", items);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        RecipeUpdateResDto recipeUpdateResDto = recipeService.updateRecipe(recipeUpdateReqDto, id, userName);
        assertThat(recipeUpdateResDto.getRecipeTitle()).isEqualTo("수정제목");
        assertThat(recipeUpdateResDto.getRecipeBody()).isEqualTo("수정내용");
        assertThat(recipeUpdateResDto.getUserName()).isEqualTo(userName);
    }

    @Test
    void 레시피_수정_실패_다른유저가_시도한경우() {

        RecipeUpdateReqDto recipeUpdateReqDto = new RecipeUpdateReqDto("수정제목", "수정내용", items);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        AppException appException = assertThrows(AppException.class, () -> {
            recipeService.updateRecipe(recipeUpdateReqDto, id, userName2);
        });
        assertEquals(ErrorCode.INVALID_PERMISSION, appException.getErrorCode());
    }

    @Test
    void 레시피_삭제_성공() {

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        assertDoesNotThrow(() -> recipeService.deleteRecipe(id, userName));

    }

    @Nested
    @DisplayName("좋아요 기능 테스트")
    class LikeTest {
        @Test
        @DisplayName("좋아요 등록")
        void pushLikeTest() {
            // given
            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
            when(likeRepository.findByUserAndRecipe(userEntity, recipeEntity)).thenReturn(Optional.empty());

            // when
            String message = recipeService.pushLikes(id, userName);

            // then
            assertEquals( "좋아요를 눌렀습니다.", message);
            verify(likeRepository, atLeastOnce()).save(any(LikeEntity.class));  // LikeRepository의 save()가 최소 한번 호출됐는지 검증
        }

        @Test
        @DisplayName("좋아요 취소")
        void cancelLikeTest() {
            // given
            LikeEntity likeEntity = LikeEntityFixture.get(userEntity, recipeEntity);

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipeEntity));
            when(likeRepository.findByUserAndRecipe(userEntity, recipeEntity)).thenReturn(Optional.of(likeEntity));

            // when
            String message = recipeService.pushLikes(id, userName);

            // then
            assertEquals( "좋아요를 취소합니다.", message);
            verify(likeRepository, atLeastOnce()).delete(likeEntity);  // LikeRepository의 delete()가 최소 한번 호출됐는지 검증
        }
    }
}