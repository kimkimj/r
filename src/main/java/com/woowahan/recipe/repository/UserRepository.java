package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName); // 아이디 중복확인
    Optional<UserEntity> findByEmail(String email); // 이메일 중복확인

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("select u from UserEntity u where u.userRole = 'USER'")
    Page<UserEntity> findUserByUserRole(Pageable pageable);

    @Query("select u from UserEntity u where u.userRole <> :role")
    Page<UserEntity> findByUserRoleNot(Pageable pageable, @Param("role") UserRole userRole);
}
