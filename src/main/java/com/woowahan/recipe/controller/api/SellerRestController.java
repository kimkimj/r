package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.itemDto.ItemListResDto;
import com.woowahan.recipe.domain.dto.sellerDto.*;
import com.woowahan.recipe.service.ItemService;
import com.woowahan.recipe.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SellerRestController {
    private final SellerService sellerService;
    private final ItemService itemService;

    // 회원가입
    @PostMapping("/seller/join")
    public Response<SellerJoinResponse> join(@RequestBody SellerJoinRequest sellerJoinRequest) {
        SellerJoinResponse sellerJoinResponse = sellerService.join(sellerJoinRequest);
        return Response.success(sellerJoinResponse);
    }

    @GetMapping("/seller/check-sellerName")
    public String checkSellerName(String sellerName) {
        if (!sellerService.checkSellerName(sellerName)) {
            return "사용 가능한 아이디 입니다.";
        } else {
            return "중복된 아이디 입니다.";
        }
    }

    @GetMapping("/seller/check-email")
    public String checkEmail(String email) {
        if (!sellerService.checkEmail(email)) {
            return "사용가능한 이메일 입니다.";
        } else {
            return "중복된 이메일 입니다.";
        }
    }

    @GetMapping("/seller/check-account")
    public String checkAccount(String sellerName) {
        if (!sellerService.checkSellerName(sellerName)) {
            return "존재하지 않는 아이디입니다.";
        }
        return "아이디 존재";
    }

    @GetMapping("/seller/check-password")
    public String checkPassword(String sellerName, String password) {
        if (!sellerService.checkPassword(sellerName, password)) {
            return "비밀번호가 틀렸습니다.";
        }
        return "로그인 성공";
    }


    // 로그인
    @PostMapping("/seller/login")
    public Response<SellerLoginResponse> login(@RequestBody SellerLoginRequest sellerLoginRequest) {
        String token = sellerService.login(sellerLoginRequest.getSellerName(), sellerLoginRequest.getPassword());
        return Response.success(new SellerLoginResponse(token));
    }

    // 판매자 단건 조회
    @GetMapping("/seller/{sellerName}")
    public Response<SellerResponse> findById(@PathVariable String sellerName, Authentication authentication) {
        return Response.success(sellerService.findBySellerName(sellerName));
    }

    // 판매자 전체 조회
    @GetMapping("/sellers")
    public Response<Page<SellerListResponse>> findAll(Pageable pageable) {
        return Response.success(sellerService.findAll(pageable));
    }


    // 판배자 정보 수정
    @PutMapping("/seller/{sellerName}")
    public Response<SellerResponse> update(@PathVariable String sellerName,
                                           @RequestBody SellerUpdateRequest sellerUpdateRequest) {
        SellerResponse sellerResponse= sellerService.update(sellerName, sellerUpdateRequest);
        return Response.success(sellerResponse);
    }


    @DeleteMapping("/seller/{sellerName}")
    public Response<SellerDeleteResponse> delete(@PathVariable String sellerName) {
        SellerDeleteResponse sellerDeleteResponse = sellerService.deleteSeller(sellerName);
        return Response.success(sellerDeleteResponse);
    }

    // 판매자가 등록한 재료 전체 조회
    @GetMapping("/seller/items/{sellerName}")
    public Response<Page<ItemListResDto>> findAllBySeller(@PathVariable String sellerName, Pageable pageable) {
        return Response.success(itemService.findAllBySeller(sellerName, pageable));
    }
}
