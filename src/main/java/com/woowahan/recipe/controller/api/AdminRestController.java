package com.woowahan.recipe.controller.api;

import com.woowahan.recipe.domain.dto.Response;
import com.woowahan.recipe.domain.dto.adminDto.AdminResponse;
import com.woowahan.recipe.domain.dto.adminDto.SellerRoleReq;
import com.woowahan.recipe.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService adminService;

    @PutMapping("/role/{id}")
    public Response<AdminResponse> updateRole(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        AdminResponse res = adminService.updateRole(id, userName);
        return Response.success(res);
    }

    @DeleteMapping("/user/{id}")
    public Response<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        adminService.deleteUser(id, userName);
        return Response.success("회원이 삭제되었습니다");
    }

    @PutMapping("/seller")
    public Response<AdminResponse> updateSeller(@RequestBody SellerRoleReq req, Authentication authentication) {
        String userName = authentication.getName();
        AdminResponse res = adminService.updateSeller(req, userName);
        return Response.success(res);
    }

    @DeleteMapping("/seller/{id}")
    public Response<String> deleteSeller(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        adminService.deleteSeller(id, userName);
        return Response.success("판매자가 삭제되었습니다");
    }
}
