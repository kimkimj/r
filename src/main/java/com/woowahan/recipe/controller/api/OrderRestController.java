package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.orderDto.*;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.exception.ErrorResult;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderRestController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    //** 주문 **//
    @GetMapping("/{id}")
    public Response<OrderInfoResponse> findOrder(@PathVariable Long id, Authentication authentication) {
        OrderInfoResponse orderInfoResponse = orderService.findOrder(authentication.getName(), id);
        return Response.success(orderInfoResponse);
    }

    @GetMapping("/users/list")
    public Response<Page<OrderInfoResponse>> findMyOrder(OrderSearch orderSearch, Authentication authentication, Pageable pageable) {
        Page<OrderInfoResponse> orderInfoResponses = orderService.findMyOrder(authentication.getName(), orderSearch, pageable);
        return Response.success(orderInfoResponses);
    }

    @PostMapping("")
    public Response<OrderCreateResDto> order(@RequestBody OrderCreateReqDto reqDto, Authentication authentication) {
        OrderCreateResDto orderCreateResDto = orderService.createOrder(authentication.getName(), reqDto);
        return Response.success(orderCreateResDto);
    }

    @DeleteMapping("/{id}")
    public Response<OrderDeleteResDto> deleteOrder(@PathVariable Long id, Authentication authentication) {
        OrderDeleteResDto orderDeleteResDto = orderService.cancelOrder(authentication.getName(), id);
        return Response.success(orderDeleteResDto);
    }

    //** 주문 및 결제**//
    @PostMapping("/payment/complete")
    public Response<?> paymentComplete(@RequestBody OrderCreateReqDto orderDto, Authentication authentication) throws IOException {
        log.info("orderDto.getImpUid", orderDto.getImp_uid());
        // 1. 아임포트 API 키와 SECRET키로 토큰을 생성
        String token = paymentService.getToken();
        log.info("iamprot token={}", token);
        // 2. 토큰으로 결제 완료된 주문정보를 가져옴
        int amount = paymentService.paymentInfo(orderDto.getImp_uid(), token);
        log.info("amount={}", amount);
        // 3. 결제 누르기 전에 가격이랑 아임포트에 들어갈 가격이 일치해야 함
        try {
            // 4. 결제 완료된 금액과 실제 계산되어야 할 금액이 다를경우 결제 취소
            if (amount != orderDto.getTotalCost()) {
                paymentService.paymentCancel(token, orderDto.getImp_uid(), amount, "결제 금액 불일치");
                return Response.error(new ErrorResult(ErrorCode.MISMATCH_AMOUNT, ErrorCode.MISMATCH_AMOUNT.getMessage()));
            }
            // 5. 주문하기
            OrderCreateResDto orderResponse = orderService.createOrder(authentication.getName(), orderDto);
            log.info("주문성공했습니다.");
            return Response.success(orderResponse);
        } catch (Exception e) {
            // 6. 결제에러시 결제 취소
            paymentService.paymentCancel(token, orderDto.getImp_uid(), amount, "결제 에러");
            log.info("주문실패했습니다.");
            return Response.error(new ErrorResult(ErrorCode.INVALID_ORDER, ErrorCode.INVALID_ORDER.getMessage()));
        }
    }

}
