package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // Foreign Key
    @OneToOne
    @JoinColumn(name="cart_id")
    private CartEntity cartEntity;

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

    private Date birth;
}
