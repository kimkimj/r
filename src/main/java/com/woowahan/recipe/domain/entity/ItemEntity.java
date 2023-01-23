package com.woowahan.recipe.domain.entity;


import com.woowahan.recipe.exception.NotEnoughStockException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class ItemEntity extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //이미지가 없을 경우 기본이미지를 어떻게 넣어줄지 ? e.g) 이미지 준비중 입니다
    private String itemImagePath;
    @NotBlank
    private String itemName;
    @NotBlank
    private Integer itemPrice;
    @NotBlank
    private Integer itemStock;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    /* 아이템 수정 */
    public void update(String itemImagePath, String itemName, Integer itemPrice, Integer itemStock) {
        this.itemImagePath = itemImagePath;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
    }

    /* 연관관계 메서드 */
    public void addItem() {};

    /* 비지니스 로직 */
    public void increaseStock(int quantity) {
        this.itemStock += quantity;
    }

    public void decreaseStock(int quantity) {
         int restStock = this.itemStock -= quantity;
         if (restStock < 0) {
             throw new NotEnoughStockException("재고 수량이 없습니다.");
         }
        this.itemStock = restStock;
    }


}
