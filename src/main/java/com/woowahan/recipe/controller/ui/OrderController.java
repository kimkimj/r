package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.OrderStatus;
import com.woowahan.recipe.domain.dto.itemDto.ItemDetailResDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderCreateReqDto;
import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.service.FindService;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final FindService findService;

    /**
     * 주문하기 페이지
     * @param id
     * @param model             주문 할 때 가격 넣어주기
     * @param orderCreateReqDto 주문 할 때 가격 가져오기
     * @param authentication    인증된 사용자 검증
     * @return
     */
    @GetMapping("/items/{id}/order")
    public String orderForm(@PathVariable Long id, Model model, @ModelAttribute OrderCreateReqDto orderCreateReqDto, Authentication authentication) {
        UserResponse userResponse = findService.findUserName(authentication.getName());
        ItemDetailResDto item = findService.findItemName(id);

        model.addAttribute("item", item);
        model.addAttribute("userResponse", userResponse);
        return "order/orderForm";
    }

    @GetMapping("delivery/{orderId}")
    public String getDeliveryStatus(@PathVariable Long orderId, Model model, Authentication authentication) {
        String username = authentication.getName();
        OrderInfoResponse order = orderService.findOrderById(username, orderId);
        model.addAttribute("orderInfoResponse", order);

        Long userId = userService.findUserId(username);
        UserResponse userResponse = userService.findUser(userId);
        model.addAttribute("userResponse", userResponse);
        return "order/delivery";
    }

    @GetMapping("/orders/my")
    public String myOrders(Model model, OrderSearch orderSearch, Authentication authentication, Pageable pageable) {
        Page<OrderInfoResponse> orderList = orderService.findMyOrder(authentication.getName(), orderSearch, pageable);

        int nowPage = orderList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, orderList.getTotalPages());
        int lastPage = orderList.getTotalPages();

        model.addAttribute("orderSearch", orderSearch);
        model.addAttribute("orderList", orderList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "user/my/myOrder";

    }

    @GetMapping("/orders/{orderNum}")
    public String findOrder(@PathVariable String orderNum, Model model, Authentication authentication) {
        UserResponse user = findService.findUserName(authentication.getName());
        OrderInfoResponse order = orderService.findOrderByOrderNum(authentication.getName(), orderNum);

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        if (order.getOrderStatus().equals(OrderStatus.ORDER)) {
            return "order/detail";
        } else {
            return "order/cancel";
        }
    }
}
