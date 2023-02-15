package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.cartDto.CartItemReq;
import com.woowahan.recipe.domain.dto.itemDto.*;
import com.woowahan.recipe.domain.dto.recipeDto.RecipePageResDto;
import com.woowahan.recipe.domain.dto.sellerDto.*;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.service.ItemService;
import com.woowahan.recipe.service.S3UploadService;
import com.woowahan.recipe.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final ItemService itemService;
    private final S3UploadService s3UploadService;

    // 판매자 홈페이지
    @GetMapping("/sellerIndex")
    public String home() {
        return "seller/sellerIndex";
    }

    // 판매자 회원 가입
    @GetMapping("/seller/join")
    public String joinForm(Model model) {
        model.addAttribute("sellerJoinRequest", new SellerJoinRequest());
        return "seller/joinForm";
    }

    @PostMapping("/seller/join")
    public String join(Model model, @Valid SellerJoinRequest sellerJoinRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "seller/joinForm";
        }
        model.addAttribute("sellerJoinRequest", new SellerJoinRequest());
        sellerService.join(sellerJoinRequest);
        return "redirect:/seller/login";
    }

    // 로그인
    @GetMapping("/seller/login")
    public String loginForm(Model model) {
        model.addAttribute("sellerLoginRequest", new SellerLoginRequest());
        return "seller/loginForm";
    }

    @PostMapping("/seller/login")
    public String login(@Valid @ModelAttribute SellerLoginRequest sellerLoginRequest, BindingResult result,
                        HttpServletResponse response){
        if (result.hasErrors()) {
            return "seller/loginForm";
        }

        try {
            String token = sellerService.login(sellerLoginRequest.getSellerName(), sellerLoginRequest.getPassword());
            CookieGenerator cookieGenerator = new CookieGenerator();
            cookieGenerator.setCookieName("token");
            cookieGenerator.setCookieHttpOnly(true);
            cookieGenerator.addCookie(response, token);
            cookieGenerator.setCookieMaxAge(60 * 60 * 2);

        } catch (AppException e) {
            return "seller/loginForm";
        }

        return "redirect:/";
    }


    // 로그아웃
    @GetMapping("/seller/logout")
    public String logout(HttpServletResponse response) {

        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName("token");
        cookieGenerator.addCookie(response, "deleted");
        cookieGenerator.setCookieMaxAge(0);

        return "redirect:/";
    }

    // 마이 페이지
    @GetMapping("/seller/my")
    public String myPage(Model model, Authentication authentication) {
        SellerResponse seller = sellerService.findBySellerName(authentication.getName());
        model.addAttribute("seller", seller);
        return "seller/myInfo";
    }

    // 판매자 정보 수정 페이지
    @GetMapping("/seller/my/update")
    public String update(Model model, Authentication authentication) {
        String sellerName = authentication.getName();
        SellerResponse seller = sellerService.findBySellerName(sellerName);
        model.addAttribute("seller", seller);
        return "seller/updateForm";
    }

    @PostMapping("/seller/my/update")
    public String update(Model model, Authentication authentication, SellerUpdateRequest request) {
        String sellerName = authentication.getName();
        SellerResponse seller = sellerService.update(sellerName, request);
        model.addAttribute("seller", seller);
        return "redirect:/seller/my";
    }

    // 비밀번호 수정 페이지
    @GetMapping("/seller/my/password")
    public String passwordForm(Model model) {
        model.addAttribute("sellerPasswordUpdateRequest", new SellerPasswordUpdateRequest());
        return "seller/passwordUpdate";
    }

    @PostMapping("/seller/my/password")
    public String updatePassword(Model model, Authentication authentication,
                                 @ModelAttribute SellerPasswordUpdateRequest request) {
        model.addAttribute("sellerPasswordUpdateRequest", request);
       sellerService.updatePassword(authentication.getName(), request);
       return "redirect:/seller/my";
    }


    // 레시피 페이징 중복 코드 정리
    private String paging(Model model, Page<RecipePageResDto> allRecipes) {
        int nowPage = allRecipes.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, allRecipes.getTotalPages());
        int lastPage = allRecipes.getTotalPages();

        model.addAttribute("allRecipes", allRecipes);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "seller/recipeList";
    }

    /**
     * 내 상품 전체보기 paging
     */
    private String pagingItems(Model model, Page<ItemListResDto> items) {
        int nowPage = items.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, items.getTotalPages());
        int lastPage = items.getTotalPages();

        model.addAttribute("myItemList", items);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "seller/myItems";
    }
    /**
     * 내 상품 전체 조회
     */
    @GetMapping("/seller/my/items")
    public String myItems(Model model, Authentication authentication,
                          @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ItemListResDto> items = itemService.findAllBySeller(authentication.getName(), pageable);
        return pagingItems(model, items);
    }

    /**
     * seller - 상품 상세조회
     */
    @GetMapping("/seller/items/{id}")
    public String findSellerForm(Model model, @PathVariable Long id) {
        ItemDetailResDto resDto = itemService.findItem(id);

        model.addAttribute("id", id);
        model.addAttribute("resDto", resDto); // -> sellerName도 들어있음
        model.addAttribute("cartItemReq", new CartItemReq(id, 1));
        return "seller/itemFindForm";
    }

    /**
     * 상품 등록
     */
    @GetMapping("/seller/items/create")
    public String createForm(Model model) {
        model.addAttribute("itemCreateReqDto", new ItemCreateReqDto());
        return "seller/itemCreateForm";
    }

    @PostMapping("/seller/items/create")
    public String create(@Valid @ModelAttribute ItemCreateReqDto reqDto, BindingResult bindingResult,
                         @RequestParam MultipartFile multipartFile,
                         RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {

        if (bindingResult.hasErrors()) {
            return "seller/itemCreateForm";
        }

        if (multipartFile.isEmpty()) {
            return "redirect:/seller/items/create";
        }

        String imgPath = s3UploadService.saveUploadFile(multipartFile, "item-image");
        reqDto.setItemImagePath(imgPath);
        ItemCreateResDto resDto = itemService.createItem(reqDto, authentication.getName());
        redirectAttributes.addAttribute("id", resDto.getId());

        return "redirect:/seller/items/{id}";
    }


    /**
     * 상품 수정
     */
    @GetMapping("/seller/items/update/{id}")
    public String updateForm(@PathVariable Long id,  Model model) {
        model.addAttribute("id", id);
        model.addAttribute("itemUpdateReqDto", itemService.findItem(id));
        return "seller/itemUpdateForm";
    }

    @PostMapping("/seller/items/update/{id}")
    public String update(@Valid @ModelAttribute ItemUpdateReqDto reqDto, BindingResult bindingResult,
                         @PathVariable Long id, @RequestPart MultipartFile multipartFile,
                         RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {

        if (bindingResult.hasErrors()) {
            return "seller/itemUpdateForm";
        }
        if (!multipartFile.isEmpty()) {
            String imgPath = s3UploadService.saveUploadFile(multipartFile, "item-image");
            reqDto.setItemImagePath(imgPath);
        }

        ItemUpdateResDto resDto = itemService.updateItem(id, reqDto, authentication.getName());
        redirectAttributes.addAttribute("id", resDto.getId());
        return "redirect:/seller/items/{id}";
    }

    /**
     * 상품 삭제
     */
    @GetMapping("seller/items/delete/{id}")
    public String delete(@PathVariable Long id, Authentication authentication) {
        itemService.deleteItem(id, authentication.getName());
        return "redirect:/seller/my/items";
    }


}
