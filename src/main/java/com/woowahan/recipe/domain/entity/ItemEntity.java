package com.woowahan.recipe.domain.entity;


import com.woowahan.recipe.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ItemEntity extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemImagePath;
    @NotBlank
    @Column(name = "item_name")
    private String name;
    @NotNull
    private Integer itemPrice;
    @NotNull
    private Integer itemStock;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<RecipeItemEntity> recipes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    /* 아이템 수정 */
    public void update(String itemImagePath, String itemName, Integer itemPrice, Integer itemStock, SellerEntity seller) {
        this.itemImagePath = itemImagePath;
        this.name = itemName;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.seller = seller;
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

    // item 등록 시 해당 상품을 판매하는 판매자 등록
    public void createSeller(SellerEntity seller) {
        this.seller = seller;
    }
}
