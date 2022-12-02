package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import com.example.recipe.services.UnitOfMeasureService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/recipe/{recipeId}/")
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
            UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {

        log.debug("Getting Ingredients List for Recipe Id: ", recipeId);

        model.addAttribute("recipe", this.recipeService.findCommandById(Long.parseLong(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping("ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        model.addAttribute("ingredient",
                this.ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(recipeId), Long.parseLong(id)));

        return "recipe/ingredient/show";
    }

    @GetMapping("ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model) {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.parseLong(recipeId));
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        model.addAttribute("ingredient",
                this.ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(recipeId), Long.parseLong(id)));

        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

        IngredientCommand savedCommand = this.ingredientService.saveIngredientCommand(command);

        log.debug("Saved Recipe Id: " + savedCommand.getRecipeId());
        log.debug("Saved Ingredient Id: " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        this.ingredientService.deleteById(Long.parseLong(recipeId), Long.parseLong(id));

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
