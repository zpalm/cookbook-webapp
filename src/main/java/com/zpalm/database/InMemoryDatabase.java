package com.zpalm.database;

import com.zpalm.model.Recipe;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDatabase implements Database {

    private Map<Long, Recipe> storage;
    private AtomicLong id = new AtomicLong(0);

    public InMemoryDatabase(Map<Long, Recipe> storage) {
        this.storage = storage;
    }

    @Override
    public Recipe add(Recipe recipe) {
        if (recipe.getId() == null || !storage.containsKey(recipe.getId())) {
            return insertRecipe(recipe);
        }
        return updateRecipe(recipe);
    }

    private Recipe updateRecipe(Recipe recipe) {
        storage.replace(recipe.getId(), recipe);
        return recipe;
    }

    private Recipe insertRecipe(Recipe recipe) {
        Recipe insertedRecipe = Recipe.builder()
            .id(id.incrementAndGet())
            .name(recipe.getName())
            .ingredients(recipe.getIngredients())
            .entry(recipe.getEntry())
            .build();
        storage.put(id.get(), insertedRecipe);
        return insertedRecipe;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Optional<Recipe> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
