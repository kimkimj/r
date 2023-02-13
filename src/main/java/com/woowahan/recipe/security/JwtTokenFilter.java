package com.woowahan.recipe.security;

import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.service.FindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final JwtTokenUtils jwtTokenUtils;
    private final FindService findService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        Cookie[] cookies = request.getCookies();

        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    log.info("do token={}", token);
                }
            }
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }

        if (token == null || token.equals("deleted")) {
            filterChain.doFilter(request, response);
            return;
        }

        //토큰만료 check
        if (jwtTokenUtils.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }


        String userName = jwtTokenUtils.getUserName(token, secretKey);
        UserResponse user = findService.findUserName(userName);
        System.out.println(user.getUserName());
        System.out.println(user.getUserRole());

        // 통과
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority(user.getUserRole().getValue())));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
        /*// 헤더에 토큰없으면 거절
        try {
            // Token 꺼내기
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // authorizationHeader에 "Bearer + JwtToken"이 제대로 들어왔는지 체크
            if(authHeader == null) {

                // 화면 로그인을 위해 Session에서 Token을 꺼내보는 작업 => 여기에도 없으면 인증 실패
                // 여기에 있으면 이 Token으로 인증 진행
                HttpSession session = request.getSession(false);
                if(session == null || session.getAttribute("jwt") == null) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    authHeader = request.getSession().getAttribute("jwt").toString();
                }
            }

            if (!authHeader.startsWith("Bearer ")) {
                log.info("security:{}", "토큰이 없습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.split(" ")[1];

            // 토큰이 만료되었는지 check
            if(jwtTokenUtils.isExpired(token, secretKey)) {
                log.error("token 기간이 만료되었습니다. token={}", token);
                filterChain.doFilter(request, response);
                return;
            }

            String userName = jwtTokenUtils.getUserName(token, secretKey);
            UserResponse user = findService.findUserName(userName);
            System.out.println(user.getUserName());
            System.out.println(user.getUserRole());

            // 통과
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority(user.getUserRole().getValue())));



            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
    }*/

}
