package com.example.recipe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecipeServiceIT {

    final String newDescription = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Transactional
    @Test
    void saveOfDescription() {
        Iterable<Recipe> recipes = this.recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = this.recipeToRecipeCommand.convert(testRecipe);

        testRecipeCommand.setDescription(this.newDescription);
        RecipeCommand savedRecipeCommand = this.recipeService.saveRecipeCommand(testRecipeCommand);

        assertEquals(this.newDescription, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }
}
