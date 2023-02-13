package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.adminDto.AdminResponse;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.RecipeRepository;
import com.woowahan.recipe.repository.ReviewRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 회원정보 전체 조회
     */
    public Page<AdminResponse> findAll(Pageable pageable, String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Page<UserEntity> pages = userRepository.findUserByUserRole(pageable);
        if(user.getUserRole().equals(UserRole.ADMIN)) {
            log.info("admin 페이지로 조회");
            pages = userRepository.findUserByUserRole(pageable);
        } else if(user.getUserRole().equals(UserRole.HEAD)) {
            log.info("head 페이지로 조회");
            pages = userRepository.findByUserRoleNot(pageable, user.getUserRole());
        }
        return pages.map(AdminResponse::toAdminResponse);
    }

    /**
     * 회원등급 관리자로 변경
     */
    public AdminResponse updateRole(Long id, String username) {
        UserEntity head = validateUserByUserName(username);

        UserEntity targetUser = validateUserById(id);

        if(!head.getUserRole().equals(UserRole.HEAD)) {  // 현재 로그인한 사용자가 HEAD가 아니라면
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }

        if(targetUser.getUserRole().equals(UserRole.ADMIN)) {
            log.info("ADMIN을 USER로 변경합니다.");
            targetUser.updateRole(UserRole.USER);
        } else if(targetUser.getUserRole().equals(UserRole.USER)) {
            log.info("USER를 ADMIN으로 변경합니다.");
            targetUser.updateRole(UserRole.ADMIN);
        }
        userRepository.flush();
        log.info("변경된 권한 : {}", targetUser.getUserRole());

        return AdminResponse.toAdminResponse(targetUser);
    }

    public void deleteUser(Long id, String userName) {
        UserEntity admin = validateUserByUserName(userName);
        UserEntity targetUser = validateUserById(id);

        if(admin.getUserRole().equals(UserRole.ADMIN) && !targetUser.getUserRole().equals(UserRole.USER)) {  // 현재 로그인한 사용자가 admin이고 삭제하고자 하는 회원의 등급이 USER가 아니라면
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        } else if(admin.getUserRole().equals(UserRole.HEAD) && targetUser.getUserRole().equals(UserRole.HEAD)) {  // 현재 로그인한 사용자가 head이고 삭제하고자 하는 회원의 등급이 HEAD라면
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }

        recipeRepository.deleteByUser(targetUser);
        reviewRepository.deleteByUser(targetUser);

        log.info("사용자를 삭제합니다.");
        userRepository.deleteById(id);
    }

    public UserEntity validateUserByUserName(String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        return user;
    }

    private UserEntity validateUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        return user;
    }
}
