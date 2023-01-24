package com.woowahan.recipe.event;

import com.woowahan.recipe.domain.entity.AlarmEntity;
import com.woowahan.recipe.domain.entity.AlarmType;
import com.woowahan.recipe.domain.entity.UserEntity;

public class AlarmEvent {
    private AlarmEntity alarm;

    private AlarmEvent(AlarmEntity alarm) {
        this.alarm = alarm;
    }

    public static AlarmEvent of(AlarmType alarmType, UserEntity loginUser, UserEntity writer) {
        return new AlarmEvent(AlarmEntity.builder()
                .alarmType(alarmType)
                .fromUser(loginUser.getId())
                .targetUser(writer)
                .build());
    }

    public AlarmEntity getAlarm() {
        return alarm;
    }
}
