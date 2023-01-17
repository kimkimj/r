package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private UserEntity user_id;

    public static RecipeFindResDto from(RecipeEntity recipeEntity) {
        return new RecipeFindResDto(
                recipeEntity.getId(), recipeEntity.recipe_title, recipeEntity.recipe_body
                ,recipeEntity.user_id.getNickname(), recipeEntity.getRecipe_like(), recipeEntity.getRecipe_view()
        );
    }
}
