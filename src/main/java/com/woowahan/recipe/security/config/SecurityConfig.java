package com.woowahan.recipe.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.security.JwtTokenFilter;
import com.woowahan.recipe.security.JwtTokenUtils;
import com.woowahan.recipe.security.exception.JwtExceptionFilter;
import com.woowahan.recipe.service.FindService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.woowahan.recipe.exception.ErrorCode.INVALID_PERMISSION;
import static com.woowahan.recipe.exception.ErrorCode.ROLE_FORBIDDEN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;
    private final JwtTokenUtils jwtTokenUtils;
    private final FindService findService;

    private final String SWAGGER = "/swagger-ui/**";

    private final String[] POST_PERMIT = {
            // api
            "/api/v1/users/join",
            "/api/v1/users/login",
            "/api/v1/seller/join",
            "/api/v1/seller/login",
            "/api/v1/items/search",
            // ui
            "/login",
            "/join",
            "/users/join",
            "/users/login",
            "/seller/join",
            "/seller/login"
    };

    private final String[] GET_AUTHENTICATED = {
            // UserController
            "/users/my",
            "/users/my/**",
            "/users/logout",
            "/admin",
            "/admin/**",
            "/api/v1/admin/**",
            // /api/v1/users/ 관련 부분 수정필요
            "/api/v1/users/**",
            // SellerController
            "/seller/my",
            "/seller/my/**",
            "/seller/items/**",
            "/seller/logout",
            "/api/v1/seller/**",
            // RecipeController
            "/recipes/create",
            "/recipes/update/**",
            "/recipes/delete/**",
            "/recipes/**/likes",
            "/recipes/my",
            "/recipes/likes/my",
            "/api/v1/recipes/my",
            "/apu/v1/recipes/reviews",
            // OrderController
            "/items/**/order",
            "/delivery/**",
            "/orders/**",
            "/api/v1/orders/**",
            // ItemController
            // CartController
            "/carts/**",
            "/api/v1/carts",
            // AlaramController
            "/api/v1/alarms"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(HttpMethod.POST, POST_PERMIT).permitAll()
                .antMatchers("/seller/login", "/seller/join").permitAll()
                .antMatchers("/admin", "/admin/**").hasAnyRole("ADMIN", "HEAD")
                .antMatchers("/head/**").hasRole("HEAD")
                .antMatchers("/seller/my", "/seller/my/update", "/seller/my/password", "/seller/logout").hasAnyRole("SELLER", "READY", "REJECT")
                .antMatchers("/seller/**").hasRole("SELLER")
                .antMatchers(HttpMethod.GET, GET_AUTHENTICATED).authenticated()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.PATCH).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated();

        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .addFilterBefore(new JwtTokenFilter(secretKey, jwtTokenUtils, findService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtTokenFilter.class);

        httpSecurity
                .exceptionHandling()
                // 인증 실패 시 INVALID_PERMISSION 에러 발생
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.sendRedirect("/login");
                        makeErrorResponse(response, INVALID_PERMISSION);
                    }
                })
                // 인가 실패 시 ROLE_FORBIDDEN 에러 발생
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                        response.sendRedirect("/error/403");
                        makeErrorResponse(response, ROLE_FORBIDDEN);
                    }
                });

        return httpSecurity.build();
    }

    // Security Filter Chain에서 발생하는 Exception은 ExceptionManager 까지 가지 않기 때문에 여기서 직접 처리
    public void makeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), Response.error(INVALID_PERMISSION.getMessage()));
    }
}
