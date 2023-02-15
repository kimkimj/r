package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.itemDto.ItemListForRecipeResDto;
import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.domain.entity.*;
import com.woowahan.recipe.event.AlarmEvent;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ItemRepository itemRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final ApplicationEventPublisher publisher;
    private final S3UploadService s3UploadService;

    /**
     * @param recipeId
     * @return recipeFindResDto
     * @author 김응준
     * @date 2023-01-17
     * @description ID로 레시피 단건조회
     **/
    public RecipeFindResDto findRecipe(Long recipeId) {
        RecipeEntity recipeEntity = validateRecipe(recipeId);
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(recipeId);
        RecipeFindResDto recipeFindResDto = RecipeEntity.from(optRecipe.get());
        recipeItemRepository.findRecipeItemEntitiesByRecipe(recipeEntity).orElseThrow(() -> {
            throw new AppException(ErrorCode.RECIPE_ITEM_NOT_FOUND, ErrorCode.RECIPE_ITEM_NOT_FOUND.getMessage());
        });
        return recipeFindResDto;
    }

    /**
     * @param pageable
     * @return Page<RecipePageResDto>
     * @author 김응준
     * @date 2023-01-20
     * @description 레시피 전체 조회
     **/
    public Page<RecipePageResDto> findAllRecipes(Pageable pageable) {
        Page<RecipeEntity> recipeEntities = recipeRepository.findAll(pageable);
        return recipeEntities.map(RecipeEntity::toResponse);
    }

    /**
     * @param pageable
     * @param userName
     * @return Page<RecipePageResDto>
     * @author 김응준
     * @date 2023-01-20
     * @description 레시피 마이피드
     **/
    public Page<RecipePageResDto> myRecipes(Pageable pageable, String userName) {
        UserEntity user = validateUserName(userName);
        validateRecipe(user);
        Page<RecipeEntity> recipeEntities = recipeRepository.findRecipeEntitiesByUser(user, pageable);
        return recipeEntities.map(RecipeEntity::toResponse);
    }

    /**
     * @param recipeCreateReqDto
     * @param userName
     * @return RecipeCreateResDto
     * @author 김응준
     * @date 2023-01-18
     * @description 레시피 작성
     **/
    public RecipeCreateResDto createRecipe(@RequestParam RecipeCreateReqDto recipeCreateReqDto, String userName) {
        RecipeEntity recipeEntity = createRecipeEntity(recipeCreateReqDto, userName);
        RecipeEntity saveRecipe = recipeRepository.save(recipeEntity);
        // 레시피 이름으로부터 id값 빼오면서 RecipeItemEntity에 저장
        for (int i = 0; i < recipeCreateReqDto.getItems().size(); i++) {
            ItemEntity itemEntity = itemRepository.findByName(recipeCreateReqDto.getItems().get(i)).orElse(null);
            RecipeItemEntity recipeItemEntity = RecipeItemEntity.builder()
                    .item(itemEntity)
                    .recipe(saveRecipe)
                    .build();
            recipeItemRepository.save(recipeItemEntity);
        }
        return new RecipeCreateResDto(saveRecipe.getId(), saveRecipe.getRecipeTitle(), saveRecipe.getRecipeBody(),
                saveRecipe.getUser().getUserName(), saveRecipe.getCreatedDate());
    }

    /**
     * @param recipeUpdateReqDto
     * @param recipeId
     * @param userName
     * @return RecipeUpdateResDto
     * @author 김응준
     * @date 2023-01-19
     * @description 레시피 수정
     **/
    public RecipeUpdateResDto updateRecipe(@RequestParam RecipeUpdateReqDto recipeUpdateReqDto, Long recipeId, String userName) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        List<RecipeItemEntity> recipeItemEntities = recipeItemRepository.findRecipeItemEntitiesByRecipe(recipe).orElseThrow(() -> new AppException(ErrorCode.RECIPE_ITEM_NOT_FOUND, ErrorCode.RECIPE_ITEM_NOT_FOUND.getMessage()));
        validateWriterAndUserName(userName, recipe); // 동일 유저인지 검증

        // TODO: 2023-01-24 를 사용하는 SnakeCase보다는 CamelCase가 Java 프로그래밍에서 권장되는 표기법이라고 합니다 🙂
        recipe.setRecipeTitle(recipeUpdateReqDto.getRecipeTitle());
        recipe.setRecipeBody(recipeUpdateReqDto.getRecipeBody());
        recipeItemRepository.deleteAll(recipeItemEntities);
        if(recipeUpdateReqDto.getRecipeImagePath() != null) { // 이미지가 있으면 등록
            recipe.setRecipeImagePath(recipeUpdateReqDto.getRecipeImagePath());
        }
        RecipeEntity saveRecipe = recipeRepository.saveAndFlush(recipe);

        // TODO : 재료를 db에 등록하는 부분
        List<String> newItemList = recipeUpdateReqDto.getItems().stream().distinct().collect(Collectors.toList()); // 재료 요청 중복처리
        for (int i = 0; i < newItemList.size(); i++) {
                ItemEntity itemEntity = itemRepository.findByName(newItemList.get(i)).orElse(null);
                RecipeItemEntity recipeItemEntity = RecipeItemEntity.builder()
                        .item(itemEntity)
                        .recipe(saveRecipe)
                        .build();
                recipeItemRepository.save(recipeItemEntity);
        }
        return new RecipeUpdateResDto(recipe.getId(), recipe.getRecipeTitle(), recipe.getRecipeBody(),
                recipe.getUser().getUserName(), recipe.getLastModifiedDate());
    }

    /**
     * @param recipeId
     * @param userName
     * @return RecipeResponse
     * @author 김응준
     * @date 2023-01-20
     * @description 레시피 삭제
     **/
    public RecipeResponse deleteRecipe(Long recipeId, String userName) {
        RecipeEntity recipe = validateRecipe(recipeId);
        validateWriterAndUserName(userName, recipe); // 동일 유저 검증
        recipeRepository.delete(recipe);

        return new RecipeResponse("레시피를 삭제했습니다.", recipeId);
    }

    /**
     * @param id
     * @return int
     * @author 김응준
     * @date 2023-01-19
     * @description 조회수 증가
     **/
    public int updateView(Long id) {
        return recipeRepository.updateView(id);
    }

    /**
     * @param id
     * @param userName
     * @return String
     * @author 이소영
     * @date 2023-01-24
     * @description 좋아요를 처음 눌렀다면 "좋아요를 눌렀습니다.", 좋아요를 이미 누른 상태라면 "좋아요를 취소합니다." 반환
     * 좋아요를 눌렀을 때 레시피 작성자와 좋아요를 누른 회원이 다르다면 NEW_LIKE_ON_RECIPE 알람 등록
     **/
    public String pushLikes(Long id, String userName) {
        UserEntity user = validateUserName(userName);
        RecipeEntity recipe = validateRecipe(id);
        Optional<LikeEntity> optLike = likeRepository.findByUserAndRecipe(user, recipe);
        if (optLike.isPresent()) {
            likeRepository.delete(optLike.get());
            recipeRepository.decreaseLikeCounts(id);
            return "좋아요를 취소합니다.";
        } else {
            likeRepository.save(LikeEntity.of(user, recipe));
            recipeRepository.increaseLikeCounts(id);
            if (!user.equals(recipe.getUser())) {  // 현재 좋아요를 누른 사람과 레시피 작성자가 일치하지 않다면 알람 등록
                publisher.publishEvent(AlarmEvent.of(AlarmType.NEW_LIKE_ON_RECIPE, user, recipe.getUser(), recipe));
            }
            return "좋아요를 눌렀습니다.";
        }
    }

    /**
     * @param id
     * @return Integer
     * @author 이소영
     * @date 2023-01-24
     * @description 좋아요 개수를 반환
     **/
    public Integer countLikes(Long id) {
        RecipeEntity recipe = validateRecipe(id);
        Integer likeCnt = likeRepository.countByRecipe(recipe);
        return likeCnt;
    }

    /**
     * @param recipeCreateReqDto
     * @param userName
     * @return RecipeEntity
     * @author 김응준
     * @date 2023-01-18
     * @description 레시피 작성 엔티티 생성 (공통로직)
     **/
    public RecipeEntity createRecipeEntity(RecipeCreateReqDto recipeCreateReqDto, String userName) {

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipeTitle(recipeCreateReqDto.getRecipeTitle())
                .recipeBody(recipeCreateReqDto.getRecipeBody())
                .user(userRepository.findByUserName(userName).orElseThrow(() ->
                        new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()))) // 현재 로그인된 userName으로 userEntity 저장
                .recipeImagePath(recipeCreateReqDto.getRecipeImagePath())
                .build();
        return recipeEntity;
    }

    /**
     * @param userName
     * @return UserEntity
     * @author 이소영
     * @date 2023-01-24
     * @description userName을 이용하여 현재 로그인한 회원이 존재하는지 검증 (공통로직)
     **/
    private UserEntity validateUserName(String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
                });
        return user;
    }

    /**
     * @param id
     * @return RecipeEntity
     * @author 이소영
     * @date 2023-01-25
     * @description recipeId를 이용하여 현재 조회하고자 하는 레시피가 존재하는지 검증 (공통로직)
     **/
    private RecipeEntity validateRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage());
        });
    }

    /**
     * @param user
     * @return List<RecipeEntity>
     * @author 김응준
     * @date 2023-01-26
     * @description UserEntity user를 이용하여 현재 조회하고자 하는 레시피가 존재하는지 검증 (공통로직)
     **/
    private List<RecipeEntity> validateRecipe(UserEntity user) {
        return recipeRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage());
        });
    }

    /**
     * @param userName
     * @param recipeEntity
     * @return
     * @author 김응준
     * @date 2023-01-19
     * @description 레시피 작성자와 로그인한 유저가 같은지 검증 (공통로직)
     **/
    public void validateWriterAndUserName(String userName, RecipeEntity recipeEntity) {
        if (!recipeEntity.getUser().getUserName().equals(userName)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    /**
     * @param keyword
     * @param pageable
     * @return Page<RecipePageResDto>
     * @author 이다온
     * @date 2023-01-31
     * @description 전체조회 페이지에서 레시피 검색
     **/
    public Page<RecipePageResDto> searchRecipes(String keyword, Pageable pageable) {
        Page<RecipeEntity> recipeEntities = recipeRepository.findByRecipeTitleContaining(keyword, pageable);

        // 레시피가 없는 경우
        if (recipeEntities.getSize() == 0) {
            throw new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage());
        }
        return recipeEntities.map(RecipeEntity::toResponse);
    }

    /**
     * @param keyword
     * @param pageable
     * @return Page<ItemListForRecipeResDto>
     * @author 김응준
     * @date 2023-02-01
     * @description 레시피등록->재료등록->검색->결과페이지
     */
    public Page<ItemListForRecipeResDto> searchItemPage(String keyword, Pageable pageable) {
        Page<ItemEntity> items = itemRepository.findByNameContaining(keyword, pageable);
        if (items.getSize() == 0) { //재료 검색시 키워드에 맞는 재료가 없으면 에러메세지 출력 -> 나중에 프론트에서 다시 처리 해줘야 할 듯
            throw new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage());
        }
        return items.map(ItemListForRecipeResDto::from);
    }

}
