package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.domain.entity.CartItemEntity;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.SellerEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.CartItemRepository;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ItemService {

    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    //[중복 로직] item 존재 확인 + 가져오기
    public ItemEntity validateItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }
    //[중복 로직] seller 존재 확인 + 가져오기
    private SellerEntity validateSeller(String userName) {
        return sellerRepository.findBySellerName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getMessage()));
    }

    /**
     * 재료 전체 조회
     */
    public Page<ItemListResDto> findAllItem(Pageable pageable) {
        Page<ItemEntity> items = itemRepository.findAll(pageable);
        return items.map(ItemListResDto::from);
    }

    // 특정 판매자의 재료 전체 조회
    public Page<ItemListResDto> findAllBySeller(String sellerName, Pageable pageable) {
        // seller가 존재하는지 확인
        SellerEntity seller = sellerRepository.findBySellerName(sellerName)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getMessage()));
        Page<ItemEntity> items = itemRepository.findAllBySeller(seller, pageable);
        return items.map(ItemListResDto::from);
    }

    /**
     * 재료 페이지에서 재료 검색
     */
    public Page<ItemListResDto> searchItem(String keyword, Pageable pageable) {
        Page<ItemEntity> items = itemRepository.findByNameContaining(keyword, pageable);
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
        SellerEntity seller = validateSeller(userName);  // 판매자인지 확인
        ItemEntity item = ReqDto.toEntity();
        item.createSeller(seller);  // 해당 item을 판매하는 판매자 등록
        ItemEntity savedItem = itemRepository.save(item);
        return ItemCreateResDto.from(savedItem);
    }

    /**
     * 재료 수정(관리자)
     */
    public ItemUpdateResDto updateItem(Long id, ItemUpdateReqDto ReqDto, String userName) {
        SellerEntity seller = validateSeller(userName); // seller 존재 확인+가져오기
        ItemEntity item = validateItem(id); //item 존재 확인 +가져오기
        if(!item.getSeller().equals(seller)) {  // 현재 수정하고자 하는 item을 등록한 판매자와 로그인한 회원이 동일한지
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }
        String img = item.getItemImagePath();
        item.update(ReqDto.getItemImagePath(), ReqDto.getItemName(), ReqDto.getItemPrice(), ReqDto.getItemStock(), seller);
        if(ReqDto.getItemImagePath() == null) { // 이미지가 있으면 등록
            item.setItemImagePath(img);
        }
        itemRepository.flush();
        return ItemUpdateResDto.from(item);

    }

    /**
     * 재료 삭제(관리자)
     */
    @Transactional
    public ItemDeleteResDto deleteItem(Long id, String userName) {
        SellerEntity seller = validateSeller(userName); //user 존재 확인+가져오기
        ItemEntity item = validateItem(id); //item 존재 확인+가져오기
        if(!item.getSeller().equals(seller)) {  // 현재 수정하고자 하는 item을 등록한 판매자와 로그인한 회원이 동일한지
            throw new AppException(ErrorCode.ROLE_FORBIDDEN, ErrorCode.ROLE_FORBIDDEN.getMessage());
        }

        Optional<CartItemEntity> cartItem = cartItemRepository.findByItem(item);
        if(cartItem.isPresent()) {
            cartItemRepository.deleteByItem(item);
        }

        itemRepository.delete(item);
        return ItemDeleteResDto.from(id);
    }
}
