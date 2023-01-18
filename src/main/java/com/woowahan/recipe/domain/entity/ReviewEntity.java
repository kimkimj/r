package com.woowahan.recipe.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
    @Id
    @Column(name="review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    private String review_comment;

}
