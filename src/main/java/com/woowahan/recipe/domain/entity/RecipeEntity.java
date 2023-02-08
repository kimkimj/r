package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipePageResDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Column(columnDefinition = "TEXT")
    private String recipeImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "recipe")
    private List<LikeEntity> likes;

    @OneToMany(mappedBy = "recipe")
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeItemEntity> items;

    public static RecipeFindResDto from(RecipeEntity recipeEntity) {
        return new RecipeFindResDto(
                recipeEntity.getId(), recipeEntity.recipeTitle, recipeEntity.recipeBody
                ,recipeEntity.user.getUserName(), recipeEntity.getRecipeLike(), recipeEntity.getRecipeView()
                ,recipeEntity.getCreatedDate(),recipeEntity.getLastModifiedDate(), recipeEntity.getItems(),
                recipeEntity.getReviews(), recipeEntity.getRecipeImagePath()
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
                .recipeImagePath(this.recipeImagePath)
                .build();
    }

    /* 레시피 수정 */
    public void update(String recipeTitle, String recipeBody) {
        this.recipeTitle = recipeTitle;
        this.recipeBody = recipeBody;
    }

    @Builder
    public RecipeEntity(Long id, String recipeImagePath) {
        this.id = id;
        this.recipeImagePath = recipeImagePath;
    }
}
