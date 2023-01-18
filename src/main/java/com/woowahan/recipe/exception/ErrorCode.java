package com.woowahan.recipe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    ROLE_FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러입니다."),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "내용이 없습니다.");

    private HttpStatus httpStatus;
    private String message;
}
