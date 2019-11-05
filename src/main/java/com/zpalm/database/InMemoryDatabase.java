package com.zpalm.database;

import java.util.Map;
import java.util.Optional;

import com.zpalm.model.Recipe;

public class InMemoryDatabase implements Database {

    private Map<Long, Recipe> storage;

    public InMemoryDatabase(Map<Long, Recipe> storage) {
        this.storage = storage;
    }

    @Override
    public Recipe add(Recipe recipe) {
        storage.put(recipe.getId(), recipe);
        return recipe;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Optional<Recipe> get(Long id) {
        return Optional.empty();
    }
}
