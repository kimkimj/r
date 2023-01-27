package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartItemCreateReq;
import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.CartItemEntity;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.CartItemRepository;
import com.woowahan.recipe.repository.CartRepository;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<CartInfoResponse> findCartItemList(Pageable pageable, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        Page<CartInfoResponse> cartItemPage = cartItemRepository.findByCart(pageable, cart).map(CartInfoResponse::from);

        return cartItemPage;
    }

    public void createCartItem(CartItemCreateReq cartItemCreateReq, String userName) {
        UserEntity user = validateUser(userName);

        ItemEntity item = itemRepository.findById(cartItemCreateReq.getItemId())
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage()));

        if(item.getItemStock() <= 0) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
        }

        CartEntity cart = validateCart(user);
        CartItemEntity cartItem = CartItemEntity.createCartItem(cartItemCreateReq.getItemCnt(), item, cart);

        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(Long itemId, Integer itemCnt, String userName) {
        UserEntity user = validateUser(userName);

        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage()));

        if(item.getItemStock() < itemCnt) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
        }

        CartItemEntity cartItem = validateCartItem(itemId);
        cartItem.updateCartItemCnt(itemCnt);
    }

    public void deleteCartItem(Long itemId, String userName) {
        UserEntity user = validateUser(userName);

        CartItemEntity cartItem = validateCartItem(itemId);
        cartItemRepository.delete(cartItem);
    }

    private UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private CartEntity validateCart(UserEntity user) {
        return cartRepository.findByUser(user)
                .orElse(cartRepository.save(CartEntity.builder().user(user).build()));
    }

    private CartItemEntity validateCartItem(Long itemId) {
        return cartItemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND, ErrorCode.CART_ITEM_NOT_FOUND.getMessage()));
    }
}
