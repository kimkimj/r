package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.itemDto.ItemListResDto;
import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.woowahan.recipe.domain.UserRole.*;


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
    //[중복 로직] 관리자/판매자 권한 확인
    public void validateAdmin(UserEntity user) {
        if(user.getUserRole() == USER) {
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }
    }
    //[중복 로직] item 존재 확인 + 가져오기
    public ItemEntity validateItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }

    /**
     * 재료 전체 조회
     */
    public Page<ItemListResDto> findAllItem(Pageable pageable) {
        Page<ItemEntity> items = itemRepository.findAll(pageable);
        return items.map(ItemListResDto::from);
    }

    /**
     * 재료 페이지에서 재료 검색
     */
    public Page<ItemListResDto> searchItem(String keyword, Pageable pageable) {
        Page<ItemEntity> items = itemRepository.findByItemNameContaining(keyword, pageable);
        if(items.getSize() == 0) { //재료 검색시 키워드에 맞는 재료가 없으면 에러메세지 출력 -> 나중에 프론트에서 다시 처리 해줘야 할 듯
            throw new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage());
        }
        return items.map(ItemListResDto::from);
    }

    /**
     * 재료 상세 조회 (단건 조회)
     */
    public ItemDetailResDto findItem(Long id) {
        ItemEntity item = validateItem(id);
        return ItemDetailResDto.from(item);
    }

    /**
     * 재료 등록(관리자)
     */
    public ItemCreateResDto createItem(ItemCreateReqDto ReqDto, String userName) {
        UserEntity user = validateUser(userName); //user 존재 확인
        validateAdmin(user); //관리자,판매자 권한 확인
        ItemEntity item = ReqDto.toEntity();
        ItemEntity savedItem = itemRepository.save(item);
        return ItemCreateResDto.from(savedItem);
    }

    /**
     * 재료 수정(관리자)
     */
    public ItemUpdateResDto updateItem(Long id, ItemUpdateReqDto ReqDto, String userName) {
        UserEntity user = validateUser(userName); //user 존재 확인+가져오기
        validateAdmin(user); //관리자,판매자 권한 확인
        ItemEntity item = validateItem(id); //item 존재 확인+가져오기

        item.update(ReqDto.getItemImagePath(), ReqDto.getItemName(), ReqDto.getItemPrice(), ReqDto.getItemStock());
        ItemEntity savedItem = itemRepository.save(item);
        itemRepository.flush();
        return ItemUpdateResDto.from(savedItem);

    }

    /**
     * 재료 삭제(관리자)
     */
    public ItemDeleteResDto deleteItem(Long id, String userName) {
        UserEntity user = validateUser(userName); //user 존재 확인+가져오기
        validateAdmin(user); //관리자,판매자 권한 확인
        ItemEntity item = validateItem(id); //item 존재 확인+가져오기

        itemRepository.delete(item);
        return ItemDeleteResDto.from(id);
    }
}
