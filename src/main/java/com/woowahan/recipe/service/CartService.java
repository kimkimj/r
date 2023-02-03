package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.woowahan.recipe.exception.ErrorCode.SELECT_ORDER_ITEM;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    public Page<CartItemResponse> findCartItemList(Pageable pageable, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        log.info("cart : {}" , cart.getId());
        Page<CartItemResponse> cartItemPage = cartItemRepository.findByCart(cart, pageable).map(CartItemResponse::from);
        log.info("cart : {}" , cart.getId());

        return cartItemPage;
    }


    public void updateCartItem(CartItemReq cartItemUpdateReq, String userName) {
        UserEntity user = validateUser(userName);

        CartEntity cart = validateCart(user);

        CartItemEntity cartItem = validateCartItem(cart, cartItemUpdateReq.getItemId()); //cart에 item이 들어있는지 검증

        if(cartItem.getItem().getItemStock() < cartItemUpdateReq.getCartItemCnt()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
        }

        cartItem.updateCartItemCnt(cartItemUpdateReq.getCartItemCnt()); //요청받은 아이템수를 db에 저장
    }

    public void addCartItem(CartItemReq cartItemUpdateReq, String userName) {
        UserEntity user = validateUser(userName); //user 존재 검증
        CartEntity cart = validateCart(user); //user의 cart가 있는지, 존재 검증 -> 없으면 카트 생성
        ItemEntity item = validateItem(cartItemUpdateReq.getItemId()); //카트에 넣으려는 아이템이 존재하는지 확인


        Optional<CartItemEntity> cartItem = cartItemRepository.findByCartAndItemId(cart, item.getId());

        if (cartItem.isEmpty()) {
            if(item.getItemStock() < cartItemUpdateReq.getCartItemCnt()) { //아이템 stock 충분한지 확인
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
            }
            CartItemEntity cartItemEntity = CartItemEntity.createCartItem(cartItemUpdateReq.getCartItemCnt(), item, cart); //상품이 없으면 카트에 아이템 create
            cartItemRepository.save(cartItemEntity);
        } else {
            Integer cnt = cartItem.get().getCartItemCnt() + cartItemUpdateReq.getCartItemCnt();

            if(item.getItemStock() < cnt) { //아이템 stock 충분한지 확인
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK, ErrorCode.NOT_ENOUGH_STOCK.getMessage());
            }

            cartItem.get().updateCartItemCnt(cnt); //상품이 이미 카트에 있으면 아이템수만 db에 update
        }

        //1일때 -하면 아이템 삭제하기
    }

    public void deleteCartItem(Long itemId, String userName) {
        validateUser(userName);

        cartItemRepository.deleteById(itemId);
    }

    /**
     * 장바구니에 담긴 상품을 통한 주문, 주문한 상품들 장바구니에서 제거
     * @param cartOrderDtoList
     * @param userName
     * @return
     */
    public OrderCreateResDto orderCartItem(List<CartOrderDto> cartOrderDtoList, String userName) {
        // 주문 상품이 없을 경우 에러처리
        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            throw new AppException(SELECT_ORDER_ITEM, SELECT_ORDER_ITEM.getMessage());
        }

        List<OrderCreateReqDto> orderCreateReqDtoList = new ArrayList<>();
        UserEntity user = validateUser(userName);
        CartEntity cart = validateCart(user);

        // 주문한 상품을 orderCreateReqDtoList 에 담기
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItemEntity cartItem = validateCartItem(cart, cartOrderDto.getCartItemId());

            OrderCreateReqDto orderCreateReqDto = new OrderCreateReqDto();
            orderCreateReqDto.setItemId(cartItem.getId());
            orderCreateReqDto.setCount(cartItem.getCartItemCnt());
            orderCreateReqDtoList.add(orderCreateReqDto);
        }

        // 주문하기
        OrderCreateResDto orderCartItem = orderService.createOrderCartItem(orderCreateReqDtoList, userName);
        // 주문한 상품들 장바구니에서 제거
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItemEntity cartItem = validateCartItem(cart, cartOrderDto.getCartItemId());
            cartItemRepository.delete(cartItem);
        }
        return orderCartItem;
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
