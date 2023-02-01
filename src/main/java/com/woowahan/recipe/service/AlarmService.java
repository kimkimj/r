package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.alarmDto.AlarmResponseDto;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.AlarmRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public Page<AlarmResponseDto> getMyAlarms(String userName, Pageable pageable) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
                });
        Page<AlarmResponseDto> alarmResponsePage = alarmRepository.findByTargetUser(user, pageable).map(AlarmResponseDto::of);
        return alarmResponsePage;
    }
}
