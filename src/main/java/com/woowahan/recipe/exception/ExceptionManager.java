package com.woowahan.recipe.exception;

import com.woowahan.recipe.domain.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(GlobalAppException.class)
    public ResponseEntity<?> globalAppExceptionHandler(GlobalAppException e) {
        ErrorResult errorResponse = new ErrorResult(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(errorResponse));
    }
}
