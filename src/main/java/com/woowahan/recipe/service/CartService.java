package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    public Page<CartItemResponse> findCartItemList(Pageable pageable, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        log.info("cart : {}" , cart.getId());
        Page<CartItemResponse> cartItemPage = cartItemRepository.findByCart(cart, pageable).map(CartItemResponse::from);
        log.info("cart : {}" , cart.getId());

        return cartItemPage;
    }

    public void createCartItem(CartItemReq cartItemCreateReq, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        ItemEntity item = validateItem(cartItemCreateReq.getItemId());

        if(item.getItemStock() < cartItemCreateReq.getCartItemCnt()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
        }

        CartItemEntity cartItem = CartItemEntity.createCartItem(cartItemCreateReq.getCartItemCnt(), item, cart);

        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(CartItemReq cartItemUpdateReq, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        CartItemEntity cartItem = validateCartItem(cart, cartItemUpdateReq.getItemId());

        if(cartItem.getItem().getItemStock() < cartItemUpdateReq.getCartItemCnt()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
        }

        cartItem.updateCartItemCnt(cartItemUpdateReq.getCartItemCnt());
    }

    public void deleteCartItem(Long itemId, String userName) {
        validateUser(userName);

        cartItemRepository.deleteById(itemId);
    }

    /* 공통 로직 */
    private UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }
    private ItemEntity validateItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }

    private CartEntity validateCart(UserEntity user) {
        return cartRepository.findByUser(user).orElseGet(() -> cartRepository.save(CartEntity.createCart(user)));

        /*Optional<CartEntity> optCart = cartRepository.findByUser(user);

        if(cartRepository.findByUser(user).isPresent()) {
            return optCart.get();
        } else {
            return cartRepository.save(CartEntity.builder().user(user).build());
        }*/
    }

    private CartItemEntity validateCartItem(CartEntity cart, Long itemId) {
        return cartItemRepository.findByCartAndItemId(cart, itemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND, ErrorCode.CART_ITEM_NOT_FOUND.getMessage()));
    }
}
