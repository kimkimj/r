package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.alarmDto.AlarmResponseDto;
import com.woowahan.recipe.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmRestController {

    private final AlarmService alarmService;

    /**
     * @author 이소영
     * @param authentication
     * @param pageable
     * @date 2023-01-25
     * @return Page<AlarmResponseDto>
     * @description 현재 로그인한 회원의 알람 목록 조회 api
     **/

    @GetMapping
    public Response<Page<AlarmResponseDto>> getAlarms(Authentication authentication, @PageableDefault(sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = authentication.getName();
        Page<AlarmResponseDto> alarms = alarmService.getMyAlarms(userName, pageable);
        return Response.success(alarms);
    }
}
