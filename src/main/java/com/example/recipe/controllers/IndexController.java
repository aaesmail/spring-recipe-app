package com.example.recipe.controllers;

import com.example.recipe.services.RecipeService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({ "", "/", "/index", "/index.html" })
    public String getIndexPage(Model model) {
        log.debug("Getting Index page");

        model.addAttribute("recipes", this.recipeService.getRecipes());

        return "index";
    }
}
