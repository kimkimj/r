package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.itemDto.ItemListForRecipeResDto;
import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.service.FindService;
import com.woowahan.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipes")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final FindService findService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("recipeCreateReqDto", new RecipeCreateReqDto());
        return "recipe/createForm";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute RecipeCreateReqDto form, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/createForm";
        }
        String userName = authentication.getName();
        recipeService.createRecipe(form, userName);
        return "redirect:/recipes/list";
    }

    @GetMapping("/update/{recipeId}")
    public String updateForm(Model model, @PathVariable Long recipeId) {
        model.addAttribute("recipeUpdateReqDto", recipeService.findRecipe(recipeId));
        model.addAttribute("recipeId", recipeId);
        return "recipe/updateForm";
    }

    @PostMapping("/update/{recipeId}")
    public String update(@Valid @ModelAttribute RecipeUpdateReqDto form, BindingResult result, @PathVariable Long recipeId, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/updateForm";
        }
        String userName = authentication.getName();
        RecipeUpdateResDto resDto = recipeService.updateRecipe(form, recipeId, userName);
        redirectAttributes.addAttribute("recipeId", resDto.getRecipeId());
        return "redirect:/recipes/update/{recipeId}";
    }

    @GetMapping("/delete/{recipeId}")
    public String delete(@PathVariable Long recipeId, Authentication authentication) {
        String userName = authentication.getName();
        recipeService.deleteRecipe(recipeId, userName);
        return "redirect:/recipes/list";
    }

    @GetMapping("/{recipeId}")
    public String findRecipe(@PathVariable Long recipeId, Model model) {
        recipeService.updateView(recipeId);
        RecipeFindResDto recipe = recipeService.findRecipe(recipeId);
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/recipeDetailList";
    }

    @GetMapping("/{recipeId}/likes")
    public String pushLike(@PathVariable Long recipeId, Authentication authentication) {
        String userName = authentication.getName();
        recipeService.pushLikes(recipeId, userName);
        return "redirect:/recipes/{recipeId}";
    }

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecipePageResDto> allRecipes = recipeService.findAllRecipes(pageable);

        // pagination
        return paging(model, allRecipes);
    }

    @GetMapping("/my")
    public String myRecipes(Authentication authentication, Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = authentication.getName();
        Page<RecipePageResDto> myRecipes = recipeService.myRecipes(pageable, userName);

        int nowPage = myRecipes.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, myRecipes.getTotalPages());
        int lastPage = myRecipes.getTotalPages();

        model.addAttribute("myRecipes", myRecipes);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "user/my/myRecipes";
    }

    @GetMapping("/likes/my")
    public String myLikeRecipe(Model model, Authentication authentication, @PageableDefault(size = 20) Pageable pageable) {
        Page<RecipeFindResDto> myLikeRecipeList = findService.findMyLikeRecipe(authentication.getName(), pageable);

        int nowPage = myLikeRecipeList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, myLikeRecipeList.getTotalPages());
        int lastPage = myLikeRecipeList.getTotalPages();

        model.addAttribute("myLikeRecipeList", myLikeRecipeList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "user/my/myLikeRecipe";
    }

    /**
     * 레시피 검색
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute RecipeSearchReqDto recipeSearchReqDto, @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecipePageResDto> allRecipes = recipeService.searchRecipes(recipeSearchReqDto.getKeyword(), pageable);

        return paging(model, allRecipes);
    }


    @Getter
    @AllArgsConstructor
    class SearchResponse {
        private final List<String> results;
    }

    /**
     * 재료검색
     *
     * @param keyword
     * @return
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/searchItem")
    @ResponseBody
    public SearchResponse searchItem(@RequestParam String keyword) {
        // TODO: support paging
        Page<ItemListForRecipeResDto> allItems = recipeService.searchItemPage(keyword, PageRequest.of(0, 100));
        return new SearchResponse(allItems
                .stream()
                .map(ItemListForRecipeResDto::getName)
                .collect(Collectors.toList()));
    }

    /**
     * TODO : 2023-01-31 레시피 페이징 중복 코드 정리
     *
     * @param model
     * @param allRecipes
     * @return
     */
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
        return "recipe/recipeList";
    }
}

