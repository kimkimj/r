package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName); // 아이디 중복확인
    Optional<UserEntity> findByEmail(String email); // 이메일 중복확인
}
