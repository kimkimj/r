package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.domain.entity.AlarmType;
import com.woowahan.recipe.domain.entity.LikeEntity;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.event.AlarmEvent;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.LikeRepository;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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
    private final ApplicationEventPublisher publisher;

    /**
     * @author 김응준
     * @param recipe_id
     * @date 2023-01-17
     * @return recipeFindResDto
     * @description ID로 레시피 단건조회
    **/
    public RecipeFindResDto findRecipe(Long recipe_id) {
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(recipe_id);
        RecipeFindResDto recipeFindResDto = RecipeEntity.from(optRecipe.get());
        return recipeFindResDto;
    }

    /**
     * @author 김응준
     * @param pageable
     * @date 2023-01-20
     * @return Page<RecipePageResDto>
     * @description 레시피 전체 조회
     **/
    public Page<RecipePageResDto> findAllRecipes(Pageable pageable) {
        Page<RecipeEntity> recipeEntities = recipeRepository.findAll(pageable);
        return new PageImpl<>(recipeEntities.stream()
                .map(RecipeEntity::toResponse)
                .collect(Collectors.toList()));
    }

    /**
     * @author 김응준
     * @param pageable
     * @param userName
     * @date 2023-01-20
     * @return Page<RecipePageResDto>
     * @description 레시피 마이피드
     **/
    public Page<RecipePageResDto> myRecipes(Pageable pageable, String userName) {
        recipeRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        Page<RecipeEntity> recipeEntities = recipeRepository.findRecipeEntitiesByUserName(userName, pageable);
        return new PageImpl<>(recipeEntities.stream()
                .map(RecipeEntity::toResponse)
                .collect(Collectors.toList()));
    }

    /**
     * @author 김응준
     * @param recipeCreateReqDto
     * @param userName
     * @date 2023-01-18
     * @return RecipeCreateResDto
     * @description 레시피 작성
    **/
    public RecipeCreateResDto createRecipe(@RequestParam RecipeCreateReqDto recipeCreateReqDto, String userName) {
        RecipeEntity recipeEntity = createRecipeEntity(recipeCreateReqDto, userName);
        RecipeEntity saveRecipe = recipeRepository.save(recipeEntity);
        return new RecipeCreateResDto(saveRecipe.getId(), saveRecipe.getRecipe_title(), saveRecipe.getRecipe_body(),
                saveRecipe.getUser().getUserName(), saveRecipe.getCreatedDate());
    }

    /**
     * @author 김응준
     * @param recipeUpdateReqDto
     * @param recipeId
     * @param userName
     * @date 2023-01-19
     * @return RecipeUpdateResDto
     * @description 레시피 수정
    **/
    public RecipeUpdateResDto updateRecipe(@RequestParam RecipeUpdateReqDto recipeUpdateReqDto, Long recipeId, String userName) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        validateWriterAndUserName(userName, recipe); // 동일 유저인지 검증
        // TODO: 2023-01-24 _를 사용하는 SnakeCase보다는 CamelCase가 Java 프로그래밍에서 권장되는 표기법이라고 합니다 :)
        recipe.setRecipe_title(recipeUpdateReqDto.getRecipe_title());
        recipe.setRecipe_body(recipeUpdateReqDto.getRecipe_body());
        recipeRepository.saveAndFlush(recipe);
        return new RecipeUpdateResDto(recipe.getId(), recipe.getRecipe_title(), recipe.getRecipe_body(),
                recipe.getUser().getUserName(), recipe.getLastModifiedDate());
    }

    /**
     * @author 김응준
     * @param recipeId
     * @param userName
     * @date 2023-01-20
     * @return RecipeResponse
     * @description 레시피 삭제
    **/
    public RecipeResponse deleteRecipe(Long recipeId, String userName) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        validateWriterAndUserName(userName, recipe); // 동일 유저 검증
        recipeRepository.deleteById(recipeId);

        return new RecipeResponse("레시피를 삭제했습니다.", recipeId);
    }

    /**
     * @author 김응준
     * @param id
     * @date 2023-01-19
     * @return int
     * @description 조회수 증가
    **/
    public int updateView(Long id) {
        return recipeRepository.updateView(id);
    }

    /**
     * @author 이소영
     * @param id
     * @param userName
     * @date 2023-01-24
     * @return String
     * @description 좋아요를 처음 눌렀다면 "좋아요를 눌렀습니다.", 좋아요를 이미 누른 상태라면 "좋아요를 취소합니다." 반환
    **/
    public String pushLikes(Long id, String userName) {
        UserEntity user = validateUserName(userName);
        RecipeEntity recipe = validateRecipe(id);
        Optional<LikeEntity> optLike= likeRepository.findByUserAndRecipe(user, recipe);
        if(optLike.isPresent()) {
            likeRepository.delete(optLike.get());
            return "좋아요를 취소합니다.";
        }else {
            likeRepository.save(LikeEntity.of(user, recipe));
            if(!user.equals(recipe.getUser())) {  // 현재 좋아요를 누른 사람과 레시피 작성자가 일치하지 않는 경우
                publisher.publishEvent(AlarmEvent.of(AlarmType.NEW_LIKE_ON_RECIPE, user, recipe.getUser()));
            }
            return "좋아요를 눌렀습니다.";
        }
    }

    /**
     * @author 이소영
     * @param id
     * @param userName
     * @date 2023-01-24
     * @return Integer
     * @description 좋아요 개수를 반환
    **/
    public Integer countLikes(Long id, String userName) {
        RecipeEntity recipe = validateRecipe(id);
        Integer likeCnt = likeRepository.countByRecipe(recipe);
        return likeCnt;
    }

    /**
     * @author 김응준
     * @param recipeCreateReqDto
     * @param userName
     * @date 2023-01-18
     * @return RecipeEntity
     * @description 레시피 작성 엔티티 생성 (공통로직)
    **/
    public RecipeEntity createRecipeEntity(RecipeCreateReqDto recipeCreateReqDto, String userName) {
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipe_title(recipeCreateReqDto.getRecipe_title())
                .recipe_body(recipeCreateReqDto.getRecipe_body())
                .user(userRepository.findByUserName(userName).orElseThrow(() ->
                        new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()))) // 현재 로그인된 userName으로 userEntity 저장
                .build();
        return recipeEntity;
    }

    /**
     * @author 이소영
     * @param userName
     * @date 2023-01-24
     * @return UserEntity
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
     * @author 이소영
     * @param id
     * @date 2023-01-25
     * @return RecipeEntity
     * @description recipeId를 이용하여 현재 조회하고자 하는 레시피가 존재하는지 검증 (공통로직)
    **/
    private RecipeEntity validateRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.RECIPE_NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND.getMessage());
        });
    }
    /**
     * @author 김응준
     * @param userName
     * @param recipeEntity
     * @date 2023-01-19
     * @return
     * @description 레시피 작성자와 로그인한 유저가 같은지 검증 (공통로직)
    **/
    public void validateWriterAndUserName(String userName, RecipeEntity recipeEntity) {
        if (!recipeEntity.getUser().getUserName().equals(userName)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

}
