package com.woowahan.recipe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type")
    private AlarmType alarmType;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private UserEntity targetUser;

    @ManyToOne
    @JoinColumn(name = "target_recipe_id")
    private RecipeEntity targetRecipe;
}
