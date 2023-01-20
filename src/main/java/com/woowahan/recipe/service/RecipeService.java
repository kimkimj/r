package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    /**
     * TODO: 2023-01-17 ID로 레시피 단건조회
     * @param recipe_id
     * @return recipeFindResDto
     */
    public RecipeFindResDto findRecipe(Long recipe_id) {
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(recipe_id);
        RecipeFindResDto recipeFindResDto = RecipeEntity.from(optRecipe.get());
        return recipeFindResDto;
    }

    /**
     * TODO: 2023-01-18 레시피 작성
     * @param recipeCreateReqDto
     * @param userName
     * @return
     */
    public RecipeCreateResDto createRecipe(@RequestParam RecipeCreateReqDto recipeCreateReqDto, String userName) {
        RecipeEntity recipeEntity = createEntity(recipeCreateReqDto, userName);
        RecipeEntity saveRecipe = recipeRepository.save(recipeEntity);
        return new RecipeCreateResDto(saveRecipe.getId(), saveRecipe.getRecipe_title(), saveRecipe.getRecipe_body(),
                saveRecipe.getUser().getUserName(), saveRecipe.getCreatedDate());
    }

    /**
     * TODO: 2023-01-19 레시피 수정
     * @param recipeUpdateReqDto
     * @param recipeId
     * @param userName
     * @return
     */
    public RecipeUpdateResDto updateRecipe(@RequestParam RecipeUpdateReqDto recipeUpdateReqDto, Long recipeId, String userName) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND,ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        validateUserName(userName, recipe); // 동일 유저인지 검증
        recipe.setRecipe_title(recipeUpdateReqDto.getRecipe_title());
        recipe.setRecipe_body(recipeUpdateReqDto.getRecipe_body());
        recipeRepository.saveAndFlush(recipe);
        return new RecipeUpdateResDto(recipe.getId(),recipe.getRecipe_title(), recipe.getRecipe_body(),
                recipe.getUser().getUserName(),recipe.getLastModifiedDate());
    }

    /**
     * TODO : 2023-01-20 레시피 삭제
     * @param recipeId
     * @param userName
     * @return
     */
    public RecipeResponse deleteRecipe(Long recipeId,String userName) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new AppException(ErrorCode.RECIPE_NOT_FOUND,ErrorCode.RECIPE_NOT_FOUND.getMessage()));
        validateUserName(userName, recipe); // 동일 유저 검증
        recipeRepository.deleteById(recipeId);

        return new RecipeResponse("레시피를 삭제했습니다.",recipeId);
    }

    /**
     * TODO: 2023-01-19 조회수 증가
     * @param id
     * @return
     */
    @Transactional
    public int updateView(Long id) {
        return recipeRepository.updateView(id);
    }

    /**
     * TODO: 2023-01-18 레시피 작성 엔티티 생성
     * @param recipeCreateReqDto
     * @param userName
     * @return
     */
    public RecipeEntity createEntity(RecipeCreateReqDto recipeCreateReqDto, String userName) {
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipe_title(recipeCreateReqDto.getRecipe_title())
                .recipe_body(recipeCreateReqDto.getRecipe_body())
                .user(userRepository.findByUserName(userName).orElseThrow(() ->
                        new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()))) // 현재 로그인된 userName으로 userEntity 저장
                .build();
        return recipeEntity;
    }

    /**
     * TODO: 2023-01-19 레시피와 로그인한 유저가 같은지 검증
     * @param userName
     * @param recipeEntity
     */
    public void validateUserName(String userName, RecipeEntity recipeEntity) {
        if (!recipeEntity.getUser().getUserName().equals(userName)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }
}
