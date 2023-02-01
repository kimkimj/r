package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderDeleteResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public Response<OrderInfoResponse> findOrder(@PathVariable Long id) {
        OrderInfoResponse orderInfoResponse = orderService.findOrder(id);
        return Response.success(orderInfoResponse);
    }

    @GetMapping("/users/orders/list")
    public Response<Page<OrderInfoResponse>> findMyOrder(@RequestBody OrderSearch orderSearch, Authentication authentication, Pageable pageable) {
        Page<OrderInfoResponse> orderInfoResponses = orderService.findMyOrder(authentication.getName(), orderSearch, pageable);
        return Response.success(orderInfoResponses);
    }

    @PostMapping("/orders")
    public Response<OrderCreateResDto> order(@RequestBody OrderCreateReqDto reqDto, Authentication authentication) {
        OrderCreateResDto orderCreateResDto = orderService.createOrder(authentication.getName(), reqDto);
        return Response.success(orderCreateResDto);
    }

    @DeleteMapping("/orders/{id}")
    public Response<OrderDeleteResDto> deleteOrder(@PathVariable Long id, Authentication authentication) {
        OrderDeleteResDto orderDeleteResDto = orderService.cancelOrder(authentication.getName(), id);
        return Response.success(orderDeleteResDto);
    }

}
