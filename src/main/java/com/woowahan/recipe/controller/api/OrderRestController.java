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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public Response<OrderInfoResponse> findOrder(@PathVariable Long id, Authentication authentication) {
        OrderInfoResponse orderInfoResponse = orderService.findOrder(authentication.getName(), id);
        return Response.success(orderInfoResponse);
    }

    @GetMapping("/users/orders/list")
    public Response<Page<OrderInfoResponse>> findMyOrder(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC)Pageable pageable,
                                                         Authentication authentication) {
        Page<OrderInfoResponse> orderInfoResponses = orderService.findAllOrder(authentication.getName(), pageable);
        return Response.success(orderInfoResponses);
    }

    // FIXME: 2023/01/27 변수명 변경하기
    @PostMapping("/users/orders/list2")
    public Response<List<OrderInfoResponse>> findMyOrder2(@RequestBody OrderSearch orderSearch, Authentication authentication) {
        List<OrderInfoResponse> orderInfoResponses = orderService.findAllOrder2(authentication.getName(), orderSearch);
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
