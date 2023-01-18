package com.woowahan.recipe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long userId;

    // Foreign Key
    @OneToOne
    @JoinColumn(name="cart_id")
    private CartEntity cartEntity;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders = new ArrayList<>();

    private String userName;
    private String password;

    @Column(unique = true)
    private String nickname;
    private String address;

    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String userRole;
    private Date birth;
}