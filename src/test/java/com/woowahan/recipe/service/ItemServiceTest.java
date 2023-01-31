package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.itemDto.ItemCreateReqDto;
import com.woowahan.recipe.domain.dto.itemDto.ItemUpdateReqDto;
import com.woowahan.recipe.domain.entity.ItemEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.ItemRepository;
import com.woowahan.recipe.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ItemServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    ItemService itemService;
    UserEntity user;
    UserEntity user2;
    ItemEntity item;
    ItemCreateReqDto itemCreateReqDto;
    ItemUpdateReqDto itemUpdateReqDto;

    @BeforeEach
    void setUp() {
        itemService = new ItemService(userRepository, itemRepository);
        user = UserEntity.builder()
                .userName("user id")
                .password("password")
                .name("user name")
                .address("address")
                .email("email@email")
                .phoneNum("01098765432")
                .userRole(UserRole.USER)
                .birth("19951106")
                .build();

        user2 = UserEntity.builder()
                .userName("user id")
                .password("password")
                .name("user name")
                .address("address")
                .email("email@email")
                .phoneNum("01098765432")
                .userRole(UserRole.SELLER)
                .birth("19951106")
                .build();

        itemCreateReqDto = new ItemCreateReqDto("image path", "name", 10000, 200);
        itemUpdateReqDto = new ItemUpdateReqDto("update image path","update name", 20000, 400);
        item = ItemEntity.builder()
                .id(1L)
                .itemImagePath(itemCreateReqDto.getItemImagePath())
                .name(itemCreateReqDto.getItemName())
                .itemPrice(itemCreateReqDto.getItemPrice())
                .itemStock(itemCreateReqDto.getItemStock())
                .build();
    }

    @Nested
    @DisplayName("아이템 등록")
    class itemCreatTest {
        @Test
        @WithMockUser
        void 아이템_등록_성공() {
            /* given */
            UserEntity mockUserEntity = mock(UserEntity.class);
            ItemEntity mockItemEntity = mock(ItemEntity.class);

            given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(mockUserEntity));
            given(itemRepository.save(any())).willReturn(mockItemEntity);

            /* when, then */
            Assertions.assertDoesNotThrow(() -> itemService.createItem(itemCreateReqDto, user.getUserName()));
        }

        @Test
        @WithMockUser
        void 아이템_등록_실패_권한없음() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user));
            given(itemRepository.save(itemCreateReqDto.toEntity())).willReturn(item);

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.createItem(itemCreateReqDto, user2.getUserName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ROLE_FORBIDDEN.getMessage(), errorMessage);
        }
    }

    @Nested
    @DisplayName("아이템 수정")
    class itemUpdateTest {
        @Test
        @WithMockUser
        void 아이템_수정_실패_아이템존재안함() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, user2.getUserName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_유저존재안함() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.empty());
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, user2.getUserName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_권한없음() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user));
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.updateItem(item.getId(),itemUpdateReqDto, user2.getUserName())).getMessage();

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
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), user2.getUserName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_유저존재안함() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.empty());
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), user2.getUserName())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
        }
        @Test
        @WithMockUser
        void 아이템_수정_실패_권한없음() {
            /* given */
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user));
            given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.deleteItem(item.getId(), user2.getUserName())).getMessage();

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
            given(userRepository.findByUserName(user2.getUserName())).willReturn(Optional.of(user2));
            given(itemRepository.findById(item.getId())).willReturn(Optional.empty());

            /* when */
            String errorMessage = Assertions.assertThrows(AppException.class, () -> itemService.findItem(item.getId())).getMessage();

            /* then */
            Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND.getMessage(), errorMessage);
        }
    }




}