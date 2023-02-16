package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.adminDto.AdminResponse;
import com.woowahan.recipe.domain.dto.adminDto.SellerRoleReq;
import com.woowahan.recipe.domain.entity.SellerEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final RecipeRepository recipeRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;

    /**
     * 회원정보 전체 조회
     */
    public Page<AdminResponse> findAll(Pageable pageable, String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Page<UserEntity> pages = userRepository.findUserByUserRole(pageable);
        if(user.getUserRole().equals(UserRole.ADMIN)) {
            pages = userRepository.findUserByUserRole(pageable);
        } else if(user.getUserRole().equals(UserRole.HEAD)) {
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
            targetUser.updateRole(UserRole.USER);
        } else if(targetUser.getUserRole().equals(UserRole.USER)) {
            targetUser.updateRole(UserRole.ADMIN);
        }
        userRepository.flush();

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

        userRepository.deleteById(id);
    }

    public AdminResponse updateSeller(SellerRoleReq req, String userName) {
        UserEntity admin = validateUserByUserName(userName);
        SellerEntity targetSeller = validateSellerById(req.getId());

        if(!admin.getUserRole().equals(UserRole.HEAD) && !admin.getUserRole().equals(UserRole.ADMIN)) {  // 현재 로그인한 사용자가 HEAD나 ADMIN이 아니라면
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }

        if(targetSeller.getUserRole().equals(UserRole.SELLER)) {
            throw new AppException(ErrorCode.SELLER_ALREADY, ErrorCode.SELLER_ALREADY.getMessage());
        }

        targetSeller.updateToSeller(req.getStatus());
        sellerRepository.flush();
        return AdminResponse.toAdminResponse(targetSeller);
    }

    public void deleteSeller(Long id, String userName) {
        UserEntity admin = validateUserByUserName(userName);
        SellerEntity targetSeller = validateSellerById(id);

        if(!admin.getUserRole().equals(UserRole.HEAD) && !admin.getUserRole().equals(UserRole.ADMIN)) {  // 현재 로그인한 사용자가 HEAD나 ADMIN이 아니라면
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }

        if(targetSeller.getUserRole().equals(UserRole.SELLER)) {
            throw new AppException(ErrorCode.SELLER_ALREADY, ErrorCode.SELLER_ALREADY.getMessage());
        }

        sellerRepository.deleteById(id);
        itemRepository.deleteAllBySeller(targetSeller);
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

    private SellerEntity validateSellerById(Long id) {
        SellerEntity seller = sellerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getMessage()));
        return seller;
    }
}
