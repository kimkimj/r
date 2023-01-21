package com.woowahan.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowahan.recipe.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_entity SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // Foreign Key
    @OneToOne
    @JoinColumn(name="cart_id")
    private CartEntity cartEntity;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders = new ArrayList<>();

    @NotBlank
    private String userName; // 유저아이디

    @NotBlank
    private String password;

    @NotBlank
    private String name; // 유저이름

    @NotBlank
    private String address;

    @Column(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNum;

    private UserRole userRole;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private String birth;

    @OneToOne
    @Column(name = "seller_id")
    SellerEntity seller;

    // soft delete: 삭제 여부 기본값 false
    private boolean deleted = Boolean.FALSE;
}
