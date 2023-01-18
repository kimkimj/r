package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    /**
     * ID로 레시피 단건조회
     * @param recipe_id
     * @return recipeFindResDto
     */
    public RecipeFindResDto findRecipe(Long recipe_id) {
        Optional<RecipeEntity> optRecipe = recipeRepository.findById(recipe_id);
        RecipeFindResDto recipeFindResDto = RecipeEntity.from(optRecipe.get());
        return recipeFindResDto;
    }

    /**
     * 레시피 작성
     * @param recipeCreateReqDto
     * @param userName
     * @return
     */
    public RecipeCreateResDto createRecipe(@RequestParam RecipeCreateReqDto recipeCreateReqDto , String userName) {
        RecipeEntity recipeEntity = createEntity(recipeCreateReqDto,userName);
        RecipeEntity saveRecipe = recipeRepository.save(recipeEntity);
        return new RecipeCreateResDto(saveRecipe.getRecipe_title(),saveRecipe.getRecipe_body(),saveRecipe.getUser().getUserName());
    }

    /**
     * 레시피 작성 엔티티 생성
     * @param recipeCreateReqDto
     * @param userName
     * @return
     */
    public RecipeEntity createEntity(RecipeCreateReqDto recipeCreateReqDto, String userName){
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipe_title(recipeCreateReqDto.getRecipe_title())
                .recipe_body(recipeCreateReqDto.getRecipe_body())
                .user(userRepository.findByUserName(userName).orElseThrow(() -> new NoSuchElementException("레시피 등록 : 유저 ID 가져올 때 에러"))) // 현재 로그인된 userName으로 userEntity 저장
                .build();
        return recipeEntity;
    }

    public void validate() {
//        UserEntity userEntity = userRepository.findByUserName().orElseThrow(() -> new NoSuchElementException());
    }
}
