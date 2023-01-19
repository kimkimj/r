package com.woowahan.recipe.domain.entity;


import com.woowahan.recipe.exception.NotEnoughStockException;
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

    /* 연관관계 메서드 */
    public void addItem() {};

    /* 비지니스 로직 */
    public void increaseStock(int quantity) {
        this.itemStock += quantity;
    }

    public void decreaseStock(int quantity) {
         int restStock = this.itemStock -= quantity;
         if (restStock < 0) {
             throw new NotEnoughStockException("재고가 부족합니다.");
         }
        this.itemStock -= quantity;
    }


}
