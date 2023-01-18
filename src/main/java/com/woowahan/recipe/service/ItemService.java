package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateResDto;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.woowahan.recipe.domain.UserRole.ADMIN;
import static com.woowahan.recipe.domain.UserRole.HEAD;


@RequiredArgsConstructor
@Service
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //[중복 로직] user 존재 확인 + 가져오기
    public UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }
    //[중복 로직] 관리자 권한 확인
    public void validateAdmin(UserEntity user) {
        if(user.getUserRole() != HEAD && user.getUserRole() != ADMIN) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    /**
     * 재료 등록(관리자)
     */
    public ItemCreateResDto createItem(ItemCreateReqDto itemCreateReqDto, String userName) {
        UserEntity user = validateUser(userName); //user 존재 확인
        validateAdmin(user); //관리자 권한 확인
        ItemEntity item = itemCreateReqDto.toEntity();
        ItemEntity savedItem = itemRepository.save(item);
        return ItemCreateResDto.from(savedItem);
    }

}
