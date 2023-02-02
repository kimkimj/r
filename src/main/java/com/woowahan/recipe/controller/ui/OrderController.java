package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.orderDto.OrderInfoResponse;
import com.woowahan.recipe.domain.dto.orderDto.search.OrderSearch;
import com.woowahan.recipe.domain.dto.userDto.UserResponse;
import com.woowahan.recipe.repository.UserRepository;
import com.woowahan.recipe.service.OrderService;
import com.woowahan.recipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final UserRepository userRepository;

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/placeOrder/{orderId}")
    public String orderForm(@PathVariable Long orderId, Model model, Authentication authentication) {
        String username = authentication.getName();
        OrderInfoResponse order = orderService.findOrder(username, orderId);
        model.addAttribute("orderInfoResponse", order);

        Long userId = userService.findUserId(username);
        UserResponse userResponse = userService.findUser(userId);
        model.addAttribute("userResponse", userResponse);
        return "order/orderForm";
    }

    @GetMapping("delivery/{orderId}")
    public String getDeliveryStatus(@PathVariable Long orderId, Model model, Authentication authentication) {
        String username = authentication.getName();
        OrderInfoResponse order = orderService.findOrder(username, orderId);
        model.addAttribute("orderInfoResponse", order);

        Long userId = userService.findUserId(username);
        UserResponse userResponse = userService.findUser(userId);
        model.addAttribute("userResponse", userResponse);
        return "order/delivery";
    }

    @GetMapping("/orders/my")
    public String myOrders(Model model, OrderSearch orderSearch, Authentication authentication,
                           @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
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
}
