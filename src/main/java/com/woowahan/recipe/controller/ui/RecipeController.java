package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.recipeDto.*;
import com.woowahan.recipe.service.RecipeService;
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

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipes")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

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
        System.out.println(form.getRecipeBody() + form.getRecipeTitle());
        String userName = authentication.getName();
        recipeService.createRecipe(form, userName);
        return "redirect:/recipes/list";
    }

    @GetMapping("/update/{recipeId}")
    public String updateForm(Model model,@PathVariable Long recipeId) {
        model.addAttribute("recipeUpdateReqDto", new RecipeUpdateReqDto());
        model.addAttribute("recipeId", recipeId);
        return "recipe/updateForm";
    }

    @PostMapping("/update/{recipeId}")
    public String update(@Valid @ModelAttribute RecipeUpdateReqDto form, BindingResult result, @PathVariable Long recipeId, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/updateForm";
        }
        String userName = authentication.getName();
        recipeService.updateRecipe(form, recipeId, userName);
        return "redirect:/recipes/list";
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

    /**
     * 레시피 검색
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute RecipeSearchReqDto recipeSearchReqDto, @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecipePageResDto> allRecipes = recipeService.searchRecipes(recipeSearchReqDto.getKeyword(), pageable);

        return paging(model, allRecipes);
    }

    /**
     * TODO : 2023-01-31 페이징 중복 코드 정리
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

