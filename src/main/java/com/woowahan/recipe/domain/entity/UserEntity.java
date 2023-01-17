package com.woowahan.recipe.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JoinColumn(name="cartId")
    private CartEntity cartEntity;

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