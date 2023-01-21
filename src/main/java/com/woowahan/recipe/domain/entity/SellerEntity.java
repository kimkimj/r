package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE seller_entity SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class SellerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String sellerName;  // 판매자 계정 아이디

    @NotBlank
    private String name;  // 업체 이름

    @NotBlank
    private String password;

    @NotBlank
    private String address;

    @NotBlank
    private String sellerNum;

    private UserRole sellerRole;

}
