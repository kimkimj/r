package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.entity.CartEntity;
import com.woowahan.recipe.domain.entity.CartItemEntity;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.fixture.ItemEntityFixture;
import com.woowahan.recipe.fixture.TestInfoFixture;
import com.woowahan.recipe.fixture.UserEntityFixture;
import com.woowahan.recipe.repository.CartItemRepository;
import com.woowahan.recipe.repository.CartRepository;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartService cartService;

    private CartRepository cartRepository = mock(CartRepository.class);
    private CartItemRepository cartItemRepository = mock(CartItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private final String userName = TestInfoFixture.get().getUserName();
    private final String password = TestInfoFixture.get().getPassword();
    private final ItemEntity item = ItemEntityFixture.get();
    private CartItemReq cartItemReq;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository, cartItemRepository, userRepository, itemRepository);
        cartItemReq = CartItemReq.builder()
                                    .itemId(item.getId())
                                    .itemCnt(3)
                                    .build();
    }

    @Nested
    @DisplayName("공통 예외 처리 테스트")
    class ExceptionTest {
        @Test
        @DisplayName("회원이 존재하지 않은 경우")
        void notFoundUser() {
            when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

            Throwable exception = assertThrows(AppException.class, () -> {
                throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
            });

            assertEquals("존재하지 않는 유저입니다", exception.getMessage());
        }

        @Test
        @DisplayName("판매하지 않는 재료인 경우")
        void notFoundItem() {
            UserEntity user = UserEntityFixture.get(userName, password);

            when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

            // service 동작 검증
            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

            try {
                cartService.createCartItem(cartItemReq, userName);
            } catch (AppException e) {
                assertEquals("재료가 존재하지 않습니다", e.getMessage());
            }

            verify(cartRepository, never()).findByUser(any());
        }
    }

    @Nested
    @DisplayName("장바구니 아이템 등록 테스트")
    class CreateCartItemTest {
        @Test
        @DisplayName("장바구니 아이템 등록 성공")
        void createCartItem() {
            Long itemId = TestInfoFixture.get().getItemId();
            UserEntity user = mock(UserEntity.class);
            CartEntity cart = mock(CartEntity.class);

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            cartService = spy(cartService);
            doNothing().when(cartService).createCartItem(cartItemReq, userName);
        }

        @Test
        @DisplayName("재료의 재고가 부족한 경우")
        void notEnoughStock() {
            UserEntity user = mock(UserEntity.class);
            CartEntity cart = mock(CartEntity.class);
            cartItemReq = CartItemReq.builder()
                                            .itemId(item.getId())
                                            .itemCnt(10)
                                            .build();

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            try {
                cartService.createCartItem(cartItemReq, userName);
            } catch (AppException e) {
                assertEquals("재고 수량이 없습니다.", e.getMessage());
            }
        }

        @Test
        @DisplayName("장바구니가 없는 경우")
        void notFoundCart() {
            UserEntity user = mock(UserEntity.class);

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
            when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

            cartService.createCartItem(cartItemReq, userName);

            verify(cartRepository, atLeastOnce()).save(any(CartEntity.class));  // 장바구니가 생성되는지 확인
        }
    }

    @Nested
    @DisplayName("장바구니 조회 테스트")
    class findCartItemListTest {
        /*@Test
        @DisplayName("장바구니가 없는 경우 Content가 비어있는지")
        void findCartItemList() {
            UserEntity user = mock(UserEntity.class);
            CartEntity cart = CartEntityFixture.get(user);
            PageRequest pageRequest = PageRequest.of(0, 10);

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            when(cartItemRepository.findByCart(pageRequest, cart)).thenReturn(Page.empty());

            Page<CartInfoResponse> result = cartService.findCartItemList(pageRequest, userName);

            assertThat(result.getContent()).isEmpty();
        }*/
    }

    @Nested
    @DisplayName("장바구니 수정 테스트")
    class UpdateCartItemTest {

        @Test
        @DisplayName("장바구니 수량 변경 실패 - 재료 재고 < 변경한 장바구니 아이템 수량")
        void notEnoughStock() {
            UserEntity user = mock(UserEntity.class);
            CartEntity cart = mock(CartEntity.class);
            CartItemEntity cartItem = CartItemEntity.builder()
                                                    .id(1L)
                                                    .cartItemCnt(10)
                                                    .item(item)
                                                    .build();

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            assertTrue(item.getItemStock() < cartItem.getCartItemCnt());
            try {
                cartService.updateCartItem(cartItemReq, userName);
            } catch (AppException e) {
                assertEquals("재고 수량이 없습니다.", e.getMessage());
            }
        }

        @Test
        @DisplayName("장바구니 수량 변경 성공")
        void updateItemCnt() {
            Long itemId = TestInfoFixture.get().getItemId();
            Integer itemCnt = 10;
            UserEntity user = mock(UserEntity.class);
            CartEntity cart = mock(CartEntity.class);

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
            when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            cartService = spy(cartService);
            doNothing().when(cartService).updateCartItem(cartItemReq, userName);
        }
    }

    @Nested
    @DisplayName("장바구니 삭제 테스트")
    class DeleteCartItemTest {

        @Test
        @DisplayName("장바구니 삭제 성공")
        void deleteCartItem() {
            Long cartItemId = TestInfoFixture.get().getItemId();

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
            when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(mock(CartItemEntity.class)));

            cartService.deleteCartItem(cartItemId, userName);
            verify(cartItemRepository, atLeastOnce()).delete(any(CartItemEntity.class));
        }

        @Test
        @DisplayName("장바구니 삭제 실패 - 장바구니 아이템을 찾을 수 없는 경우")
        void notFoundCartItem() {
            Long cartItemId = TestInfoFixture.get().getItemId();

            when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
            when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

            try {
                cartService.deleteCartItem(cartItemId, userName);
            }catch (AppException e) {
                assertEquals("장바구니에 담긴 재료를 찾을 수 없습니다.", e.getMessage());
            }
        }
    }

}