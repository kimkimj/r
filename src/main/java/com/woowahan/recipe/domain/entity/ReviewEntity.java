package com.woowahan.recipe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
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

    private String review_comment;

    public void update(String review_comment) {
        this.review_comment = review_comment;
    }

}
