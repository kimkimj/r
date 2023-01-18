package com.woowahan.recipe.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class ItemEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String itemImagePath;
    private String itemName;
    private Integer itemPrice;
    private Integer itemStock;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    //필요한지 불확실
//    @OneToMany
//    @JoinColumn(name = "recipe_id")
//    private List<RecipeEntity> recipeList;


}
