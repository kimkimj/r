package com.woowahan.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woowahan.recipe.domain.UserRole;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_entity SET deleted_date = current_timestamp WHERE user_id = ?")
@Where(clause = "deleted_date is NULL")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // Foreign Key
    @OneToOne(mappedBy = "user")
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

    public void updateUser(String userName, String password, String name,
                           String address, String email, String phoneNum, String birth) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNum = phoneNum;
        this.birth = birth;
    }

    public void updateMyPage(String password, String name, String address,
                             String email, String phoneNum, String birth) {
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNum = phoneNum;
        this.birth = birth;
    }
}
