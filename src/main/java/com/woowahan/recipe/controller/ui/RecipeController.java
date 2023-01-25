package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipePageResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeUpdateReqDto;
import com.woowahan.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
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
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("recipeCreateReqDto", new RecipeCreateReqDto());
        return "recipe/createForm";
    }

    @PostMapping("/create")
    public String create(@Valid RecipeCreateReqDto form, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/createForm";
        }
        model.addAttribute("recipeCreateReqDto", new RecipeCreateReqDto());
        recipeService.createRecipe(form, authentication.getName());
        return "redirect:/list";
    }

    @GetMapping("/update")
    public String updateForm(Model model) {
        model.addAttribute("recipeUpdateReqDto", new RecipeUpdateReqDto());
        return "recipe/updateForm";
    }

    @PutMapping("/update/{recipeId}")
    public String update(@Valid RecipeUpdateReqDto form, BindingResult result, Model model, @PathVariable Long recipeId, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/updateForm";
        }
        model.addAttribute("recipeUpdateReqDto", new RecipeUpdateReqDto());
        recipeService.updateRecipe(form, recipeId, authentication.getName());
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecipePageResDto> allRecipes = recipeService.findAllRecipes(pageable);
        model.addAttribute("allRecipes", allRecipes);
        return "recipe/recipeList";
    }

}
