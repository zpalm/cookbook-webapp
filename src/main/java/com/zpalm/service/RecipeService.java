package com.zpalm.service;

import com.zpalm.database.Database;
import com.zpalm.database.DatabaseOperationException;
import com.zpalm.model.Recipe;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final Database database;

    public RecipeService(Database database) {
        this.database = database;
    }

    public Recipe addRecipe(Recipe recipe) throws ServiceOperationException {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe is null");
        }
        if (recipe.getId() != null && database.exists(recipe.getId())) {
            throw new ServiceOperationException("Attempt to add existing recipe");
        }
        return database.save(recipe);
    }

    public Recipe updateRecipe(Recipe recipe) throws ServiceOperationException {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe is null");
        }
        if (recipe.getId() == null || !database.exists(recipe.getId())) {
            throw new ServiceOperationException("This recipe does not exist in database");
        }
        return database.save(recipe);
    }

    public void deleteRecipe(Long id) throws ServiceOperationException {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        try {
            database.delete(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred when deleting the recipe", e);
        }
    }

    public Optional<Recipe> getRecipeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        return database.getById(id);
    }

    public Collection<Recipe> getAllRecipes() {
        return database.getAll();
    }

    public void deleteAllRecipes() {
        database.deleteAll();
    }

    public boolean recipeExists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        return database.exists(id);
    }
}
