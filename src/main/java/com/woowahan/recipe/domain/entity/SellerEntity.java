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
@SQLDelete(sql = "UPDATE seller_entity SET deleted_date = current_timestamp WHERE seller_id = ?")
@Where(clause = "deleted_date is NULL")
public class SellerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "seller_no")
    private String sellerNo;  // 사업자 등록번호

    @Column(name = "seller_site")
    private String sellerUrl;  // 사업자 홈페이지 주소

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

}
