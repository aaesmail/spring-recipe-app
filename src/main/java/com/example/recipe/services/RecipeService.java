package com.example.recipe.services;

import java.util.Set;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    RecipeCommand findCommandById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    void deleteById(Long id);
}
