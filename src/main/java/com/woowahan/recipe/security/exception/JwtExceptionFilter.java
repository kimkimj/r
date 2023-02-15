package com.woowahan.recipe.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.exception.ErrorResult;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static com.woowahan.recipe.exception.ErrorCode.DATABASE_ERROR;
import static com.woowahan.recipe.exception.ErrorCode.INVALID_TOKEN;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰의 유효기간이 만료되었을 경우
            sendErrorResponse(response, INVALID_TOKEN.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않을 경우
            sendErrorResponse(response, INVALID_TOKEN.getMessage());
        } catch (NoSuchElementException e){
            // 토큰 정보에 있는 유저가 DB에 없을 경우
            sendErrorResponse(response, DATABASE_ERROR.getMessage());
        }
    }

    /**
     * jwt 예외처리 응답
     * @param response
     * @param message
     * @throws IOException
     */

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResult(INVALID_TOKEN, message)));
    }
}
