package com.woowahan.recipe.fixture;

import com.woowahan.recipe.domain.entity.AlarmEntity;
import com.woowahan.recipe.domain.entity.AlarmType;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import com.woowahan.recipe.domain.entity.UserEntity;

import java.time.LocalDateTime;

public class AlarmEntityFixture {

    public static AlarmEntity get(Long id, UserEntity fromUser, RecipeEntity recipe, AlarmType alarmType, LocalDateTime createdDate, LocalDateTime LastModifiedDate) {
        AlarmEntity alarm = AlarmEntity.builder()
                .id(id)
                .fromUser(fromUser)
                .targetRecipe(recipe)
                .alarmType(alarmType)
                .build();
        return alarm;
    }
}
