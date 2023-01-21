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
    private String recipe_title;
    private String recipe_body;
    // 조회수의 기본 값을 0으로 지정, null 불가 처리 -> null 불가능하니까 int형으로
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int recipe_like;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int recipe_view;
    private String recipe_image_path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

 /* 리뷰 개발
    @Builder.Default
    @OneToMany(mappedBy = "recipeId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 리뷰 정렬
    private List<ReviewEntity> reivews;
*/
    @Builder.Default
    @OneToMany(mappedBy = "recipe")
    private List<ItemEntity> items = new ArrayList<>();

    public static RecipeFindResDto from(RecipeEntity recipeEntity) {
        return new RecipeFindResDto(
                recipeEntity.getId(), recipeEntity.recipe_title, recipeEntity.recipe_body
                ,recipeEntity.user.getUserName(), recipeEntity.getRecipe_like(), recipeEntity.getRecipe_view()
                ,recipeEntity.getCreatedDate(),recipeEntity.getLastModifiedDate()
        );
    }

    // 레시피 수정을위한 set메서드    * 이미지 추가 예정
    public void setRecipe_title(String recipe_title) {
        this.recipe_title = recipe_title;
    }

    public void setRecipe_body(String recipe_body) {
        this.recipe_body = recipe_body;
    }

    public RecipePageResDto toResponse() {
        return RecipePageResDto.builder()
                .recipe_id(this.id)
                .recipe_title(this.recipe_title)
                .userName(this.user.getUserName())
                .recipe_view(this.recipe_view)
                .recipe_like(this.recipe_like)
//                .thumbnail_image_path(this.recipe_image_path) 썸네일 추가시
                .build();
    }
}
