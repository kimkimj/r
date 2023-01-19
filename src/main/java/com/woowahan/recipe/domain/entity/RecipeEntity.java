package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
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

    private Long recipe_like;
    private Long recipe_view;
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
        );
    }
}
