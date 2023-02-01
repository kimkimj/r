package com.woowahan.recipe.security.auth;

import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // UserDetailsService 는 DB에서 회원 정보를 가져오는 인터페이스
    // -> loadUserByUsername() 메소드를 통해 회원정보 조회 -> UserDetails 인터페이스 반환
    // UserDetails 는 회원 정보를 담는 인터페이스
    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = userRepository.findByUserName(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(ErrorCode.USERNAME_NOT_FOUND.getMessage());
        });

        return new PrincipalDetails(user);
    }
}
