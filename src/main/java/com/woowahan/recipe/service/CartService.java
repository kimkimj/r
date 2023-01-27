package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.cartDto.CartInfoResponse;
import com.woowahan.recipe.domain.entity.CartEntity;
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


    private UserEntity validateUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private CartEntity validateCart(UserEntity user) {
        return cartRepository.findByUser(user)
                .orElse(cartRepository.save(CartEntity.builder().user(user).build()));
    }
}
