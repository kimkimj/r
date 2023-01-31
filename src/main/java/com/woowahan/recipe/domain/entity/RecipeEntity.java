package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipePageResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecipeEntity extends BaseEntity {

    @Id
    @Column(name = "recipe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipeTitle;
    private String recipeBody;
    // 조회수의 기본 값을 0으로 지정, null 불가 처리 -> null 불가능하니까 int형으로
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int recipeLike;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int recipeView;
    private String recipeImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "recipe")
    private List<LikeEntity> likes;

 /* 리뷰 개발
    @Builder.Default
    @OneToMany(mappedBy = "recipeId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 리뷰 정렬
    private List<ReviewEntity> reivews;
*/
    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeItemEntity> recipeItems = new ArrayList<>();

    public static RecipeFindResDto from(RecipeEntity recipeEntity) {
        return new RecipeFindResDto(
                recipeEntity.getId(), recipeEntity.recipeTitle, recipeEntity.recipeBody
                ,recipeEntity.user.getUserName(), recipeEntity.getRecipeLike(), recipeEntity.getRecipeView()
                ,recipeEntity.getCreatedDate(),recipeEntity.getLastModifiedDate()
        );
    }

    // 레시피 수정을위한 set메서드    * 이미지 추가 예정
    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRecipeBody(String recipeBody) {
        this.recipeBody = recipeBody;
    }

    public RecipePageResDto toResponse() {
        return RecipePageResDto.builder()
                .recipeId(this.id)
                .recipeTitle(this.recipeTitle)
                .userName(this.user.getUserName())
                .recipeView(this.recipeView)
                .recipeLike(this.recipeLike)
                .createdDate(this.getCreatedDate())
                .lastModifiedDate(this.getLastModifiedDate())
//                .thumbnailImagePath(this.recipeImagePath) 썸네일 추가시
                .build();
    }
}
