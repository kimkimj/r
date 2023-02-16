package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.SellerEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.CartItemRepository;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.SellerRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ItemServiceTest {

    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    SellerRepository sellerRepository = Mockito.mock(SellerRepository.class);
    CartItemRepository cartItemRepository = Mockito.mock(CartItemRepository.class);
    ItemService itemService;
    SellerEntity seller1;
    SellerEntity seller2;
    ItemEntity item;
    ItemCreateReqDto itemCreateReqDto;
    ItemUpdateReqDto itemUpdateReqDto;

    @BeforeEach
    void setUp() {
        itemService = new ItemService(sellerRepository, itemRepository, cartItemRepository);
        seller1 = SellerEntity.builder()
                .sellerName("user id1")
                .password("password1")
                .companyName("company name1")
                .address("address1")
                .email("email1@email")
                .phoneNum("01098765432")
                .userRole(UserRole.SELLER)
                .businessRegNum("000000000001")
                .build();

        seller2 = SellerEntity.builder()
                .sellerName("seller id2")
                .password("password2")
                .companyName("company name2")
                .address("address2")
                .email("email2@email")
                .phoneNum("01012345678")
                .userRole(UserRole.SELLER)
                .businessRegNum("000000000002")
                .build();

        itemCreateReqDto = new ItemCreateReqDto("image path", "name", 10000, 200);
        itemUpdateReqDto = new ItemUpdateReqDto("update image path","update name", 20000, 400);
        item = ItemEntity.builder()
                .id(1L)
                .itemImagePath(itemCreateReqDto.getItemImagePath())
                .name(itemCreateReqDto.getItemName())
                .itemPrice(itemCreateReqDto.getItemPrice())
                .itemStock(itemCreateReqDto.getItemStock())
                .seller(seller1)
                .build();
    }

    @Nested
    @DisplayName("아이템 등록")
    class itemCreateTest {
        @Test
        @WithMockUser
        void 아이템_등록_성공() {
            /* given */
            SellerEntity mockSellerEntity = mock(SellerEntity.class);
            ItemEntity mockItemEntity = mock(ItemEntity.class);

            given(sellerRepository.findBySellerName(seller1.getSellerName())).willReturn(Optional.of(mockSellerEntity));
            given(itemRepository.save(any())).willReturn(mockItemEntity);

            /* when, then */
            Assertions.assertDoesNotThrow(() -> itemService.createItem(itemCreateReqDto, seller1.getSellerName()));
        }

        @Test
        @WithMockUser
        void 아이템_등록_실패_판매자존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller1.getSellerName())).willReturn(Optional.of(seller1));  // 기존에 아이템 등록한 판매자
            given(itemRepository.save(itemCreateReqDto.toEntity())).willReturn(item);

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.createItem(itemCreateReqDto, seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.SELLER_NOT_FOUND.getMessage(), errorMessage);
        }
    }

    @Nested
    @DisplayName("아이템 수정")
    class itemUpdateTest {
        @Test
        @WithMockUser
        void 아이템_수정_실패_아이템존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.of(seller2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_판매자존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.empty());
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.SELLER_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_권한없음() {
            /* given */
            given(sellerRepository.findBySellerName(seller1.getSellerName())).willReturn(Optional.of(seller1));  // 아이템을 등록한 판매자
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.of(seller2));  // 현재 로그인한 판매자
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ROLE_FORBIDDEN.getMessage(), errorMessage);
        }
    }

    @Nested
    @DisplayName("아이템 삭제")
    class itemDeleteTest {
        @Test
        @WithMockUser
        void 아이템_삭제_실패_아이템존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.of(seller2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_판매자존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.empty());
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.SELLER_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_권한없음() {
            /* given */
            given(sellerRepository.findBySellerName(seller1.getSellerName())).willReturn(Optional.of(seller1));  // 아이템을 등록한 판매자
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.of(seller2));  // 현재 로그인한 판매자
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), seller2.getSellerName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ROLE_FORBIDDEN.getMessage(), errorMessage);
        }
    }

    @Nested
    @DisplayName("아이템 조회")
    class itemFindTest {
        @Test
        @WithMockUser
        void 아이템_상세조회_실패_아이템존재안함() {
            /* given */
            given(sellerRepository.findBySellerName(seller2.getSellerName())).willReturn(Optional.of(seller2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.findItem(item.getId())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
    }




}