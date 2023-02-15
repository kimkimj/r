package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.cartDto.CartItemResponse;
import com.woowahan.recipe.domain.dto.cartDto.CartOrderListDto;
import com.woowahan.recipe.domain.dto.cartDto.CheckOrderItemDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.exception.ErrorResult;
import com.woowahan.recipe.service.CartService;
import com.woowahan.recipe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;
    private final PaymentService paymentService;

    @GetMapping
    public Response<Page<CartItemResponse>> findCartList(@PageableDefault(sort = "itemName", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page<CartItemResponse> cartInfoResponsePage = cartService.findCartItemList(pageable, userName);
        return Response.success(cartInfoResponsePage);
    }

    @PostMapping
    public Response<String> createCartItem (@RequestBody CartItemReq cartItemCreateReq, Authentication authentication) {
        String userName = authentication.getName();
        cartService.addCartItem(cartItemCreateReq, userName);
        return Response.success("상품이 장바구니에 담겼습니다.");
    }

    @PostMapping("/checkOrder")
    public Response<String> updateCheckItem (@RequestBody List<CheckOrderItemDto> checkCartItemDto, Authentication authentication) {
        String userName = authentication.getName();
        cartService.updateCheckItem(checkCartItemDto, userName);
        return Response.success("장바구니 상품 체크 완료");
    }


    @PostMapping("/orders")
    public Response<OrderCreateResDto> orderCartItem(@RequestBody CartOrderListDto cartOrderListDto, Authentication authentication) {
        OrderCreateResDto orderCreateResDto = cartService.orderCartItem(cartOrderListDto, authentication.getName());
        return Response.success(orderCreateResDto);
    }

    @PostMapping("/orders/payment/complete")
    public Response<?> paymentComplete(@RequestBody CartOrderListDto cartOrderListDto, Authentication authentication) throws IOException {
        String token = paymentService.getToken();
        int amount = paymentService.paymentInfo(cartOrderListDto.getImp_uid(), token);
        try {
            if (amount != cartOrderListDto.getTotalCost()) {
                paymentService.paymentCancel(token, cartOrderListDto.getImp_uid(), amount, "결제 금액 불일치");
                return Response.error(new ErrorResult(ErrorCode.MISMATCH_AMOUNT, ErrorCode.MISMATCH_AMOUNT.getMessage()));
            }
            OrderCreateResDto orderResponse = cartService.orderCartItem(cartOrderListDto, authentication.getName());
            return Response.success(orderResponse);
        } catch (Exception e) {
            paymentService.paymentCancel(token, cartOrderListDto.getImp_uid(), amount, "장바구니 결제 에러");
            return Response.error(new ErrorResult(ErrorCode.INVALID_ORDER, ErrorCode.INVALID_ORDER.getMessage()));
        }
    }

    @PutMapping
    public Response<String> updateCartItem (@RequestBody CartItemReq cartItemUpdateReq, Authentication authentication) {
        String userName = authentication.getName();
        Integer cnt = cartService.updateCartItem(cartItemUpdateReq, userName);
        return Response.success(cnt + "개로 수량이 수정되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public Response<String> deleteCartItem(@PathVariable Long itemId, Authentication authentication) {
        String userName = authentication.getName();
        cartService.deleteCartItem(itemId, userName);
        return Response.success("장바구니에서 아이템에서 삭제되었습니다.");
    }
}
