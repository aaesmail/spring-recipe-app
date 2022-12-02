package com.example.recipe.services;

import java.util.Optional;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientCommandToIngredient;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import com.example.recipe.repositories.UnitOfMeasureRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
            RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository,
            IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = this.recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("Recipe Id not found, Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> this.ingredientToIngredientCommand.convert(ingredient))
                .findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.error("Ingredient Id not found, Id: ", ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = this.recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found for Id: " + command.getRecipeId());
            return new IngredientCommand();
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(this.unitOfMeasureRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM Not Found!")));
        } else {
            Ingredient ingredient = this.ingredientCommandToIngredient.convert(command);
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }

        Recipe savedRecipe = this.recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe
                .getIngredients()
                .stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                .findFirst();

        if (!savedIngredientOptional.isPresent()) {
            savedIngredientOptional = savedRecipe
                    .getIngredients()
                    .stream()
                    .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        return this.ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }

    @Transactional
    @Override
    public void deleteById(Long recipeId, Long id) {
        Optional<Recipe> recipeOptional = this.recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            throw new RuntimeException("Delete Ingredient Recipe not found For Recipe Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .findFirst();

        if (!ingredientOptional.isPresent()) {
            throw new RuntimeException("Delete Ingredient Recipe not found For Ingredient Id: " + id);
        }

        Ingredient ingredientToDelete = ingredientOptional.get();
        recipe.getIngredients().remove(ingredientToDelete);
        ingredientToDelete.setRecipe(null);

        this.recipeRepository.save(recipe);
    }
}
