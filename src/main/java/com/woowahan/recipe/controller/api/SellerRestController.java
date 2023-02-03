package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.sellerDto.*;
import com.woowahan.recipe.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SellerRestController {
    private final SellerService sellerService;

    // 회원가입
    @PostMapping("/seller/join")
    public Response<SellerJoinResponse> join(@RequestBody SellerJoinRequest sellerJoinRequest) {
        SellerJoinResponse sellerJoinResponse = sellerService.join(sellerJoinRequest);
        return Response.success(sellerJoinResponse);
    }

    // 로그인
    @PostMapping("/seller/login")
    public Response<SellerLoginResponse> login(@RequestBody SellerLoginRequest sellerLoginRequest) {
        String token = sellerService.login(sellerLoginRequest.getUserName(), sellerLoginRequest.getPassword());
        return Response.success(new SellerLoginResponse(token));
    }

    // 판매자 단건 조회
    @GetMapping("/seller/{id}")
    public Response<SellerResponse> findById(@PathVariable Long id) {
        return Response.success(sellerService.findById(id));
    }

    /*
    // 판배자 정보 수정
    @PutMapping("/seller")
    public Response<SellerResponse> update(@RequestBody SellerUpdateRequest sellerUpdateRequest,
                                           Authentication authentication) {
        String sellerName = authentication.getName();
        SellerResponse sellerResponse= sellerService.update(sellerName, sellerUpdateRequest);
        return Response.success(sellerResponse);
    }*/


    @DeleteMapping("/seller")
    public Response<SellerDeleteResponse> delete(Authentication authentication) {
        String sellerName = authentication.getName();
        SellerDeleteResponse sellerDeleteResponse = sellerService.deleteSeller(sellerName);
        return Response.success(sellerDeleteResponse);

    }


}
