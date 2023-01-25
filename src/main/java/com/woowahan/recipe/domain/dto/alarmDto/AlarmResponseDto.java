package com.woowahan.recipe.domain.dto.alarmDto;

import com.woowahan.recipe.domain.entity.AlarmEntity;
import com.woowahan.recipe.domain.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponseDto {

    private AlarmType alarmType;
    private String fromUserName;
    private String targetUserName;
    private Long targetRecipe;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;


    public static AlarmResponseDto of(AlarmEntity alarm) {
        return AlarmResponseDto.builder()
                .alarmType(alarm.getAlarmType())
                .fromUserName(alarm.getFromUser().getUserName())
                .targetUserName(alarm.getTargetUser().getUserName())
                .createdDate(alarm.getCreatedDate())
                .lastModifiedDate(alarm.getLastModifiedDate())
                .build();
    }
}
