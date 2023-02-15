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
        alarmRepository.save(e.getAlarm());
    }
}
