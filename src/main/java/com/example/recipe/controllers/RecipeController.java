package com.example.recipe.controllers;

import javax.validation.Valid;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.RecipeService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/recipe/")
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("{id}/show")
    public String showById(@PathVariable String id, Model model) {

        model.addAttribute("recipe", this.recipeService.findById(Long.parseLong(id)));

        return "recipe/show";
    }

    @GetMapping("new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @GetMapping("{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", this.recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/recipeform";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute RecipeCommand command, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", command);

            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));

            return "recipe/recipeform";
        }

        RecipeCommand savedCommand = this.recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("{id}/delete")
    public String deleteById(@PathVariable String id) {
        log.debug("Deleting Recipe Id: ", id);

        this.recipeService.deleteById(Long.parseLong(id));

        return "redirect:/";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}
