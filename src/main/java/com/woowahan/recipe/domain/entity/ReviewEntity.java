package com.woowahan.recipe.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_date is null")
@SQLDelete(sql = "UPDATE review_entity SET deleted_date = now() WHERE review_id = ?")
public class ReviewEntity extends BaseEntity{
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

    private String reviewComment;

    public void update(String reviewComment) {
        this.reviewComment = reviewComment;
    }

}
