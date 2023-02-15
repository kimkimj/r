package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.UserRole;
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
@SQLDelete(sql = "UPDATE seller_entity SET deleted_date = current_timestamp WHERE seller_id = ?")
@Where(clause = "deleted_date is NULL")
public class SellerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "seller_name")
    private String sellerName;

    private String password;

    @Column(name = "company_name")
    private String companyName;

    private String address;

    private String email;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "business_reg_num")
    private String businessRegNum;  // 사업자 등록번호

    public void updateUser(String sellerName, String companyName,
                           String address, String email, String phoneNum) {
        this.sellerName = sellerName;
        this.companyName = companyName;
        this.address = address;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateToSeller(String status) {
        if(status.equals("ok")) {
            this.userRole = UserRole.SELLER;
        } else {
            this.userRole = UserRole.REJECT;
        }
    }
}
