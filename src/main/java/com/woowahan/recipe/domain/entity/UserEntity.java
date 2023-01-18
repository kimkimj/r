package com.woowahan.recipe.domain.entity;

import com.woowahan.recipe.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    private String name;

    @NotBlank
    private String address;

    @Column(unique = true)
    @NotBlank
    private String email;

    private String phoneNum;

    private UserRole userRole;

    private Date birth;

    // Foreign Key
    @OneToOne
    @JoinColumn(name="cartId")
    private CartEntity cartEntity;
}
