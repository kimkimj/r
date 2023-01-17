package com.woowahan.recipe.event;

import com.woowahan.recipe.domain.entity.AlarmEntity;

public class AlarmEvent {
    private AlarmEntity alarm;

    private AlarmEvent(AlarmEntity alarm) {
        this.alarm = alarm;
    }

    public AlarmEntity getAlarm() {
        return alarm;
    }
}
