package com.woowahan.recipe.event;

import com.woowahan.recipe.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventHandler {

    private final AlarmRepository alarmRepository;

    @EventListener
    @Async
    public void createAlarm(AlarmEvent e) {
        log.info("alarm 발생 : type={}", e.getAlarm().getAlarmType());
        alarmRepository.save(e.getAlarm());
        log.info("Thread : {}", Thread.currentThread().toString());
        log.info("alarm 생성 완료");
    }
}
