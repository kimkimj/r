package com.woowahan.recipe.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer id;

    @ManyToOne
    @Column(name = "user_id")
    private UserEntity user;

    // TODO: 2023-01-17 RecipeEntity 생성 후 주석 해제
    /*@ManyToOne
    @Column(name = "recipe_id")
    private RecipeEntity recipe;*/
}
