package com.zpalm.database;

import com.zpalm.model.Recipe;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "com.zpalm.database", havingValue = "in-memory")
public class InMemoryDatabase implements Database {

    private Map<Long, Recipe> storage;
    private AtomicLong nextId = new AtomicLong(0);

    public InMemoryDatabase(Map<Long, Recipe> storage) {
        this.storage = storage;
    }

    @Override
    public Recipe save(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        if (recipe.getId() == null || !exists(recipe.getId())) {
            return addRecipe(recipe);
        }
        return updateRecipe(recipe);
    }

    private Recipe addRecipe(Recipe recipe) {
        Long id = nextId.incrementAndGet();
        if (recipe.getId() == null) {
            while (exists(id)) {
                id = nextId.incrementAndGet();
            }
        } else {
            id = recipe.getId();
        }
        Recipe savedRecipe = Recipe.builder()
            .id(id)
            .name(recipe.getName())
            .ingredients(recipe.getIngredients())
            .steps(recipe.getSteps())
            .build();
        storage.put(id, savedRecipe);
        return savedRecipe;
    }

    private Recipe updateRecipe(Recipe recipe) {
        Recipe updatedRecipe = Recipe.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .ingredients(recipe.getIngredients())
            .steps(recipe.getSteps())
            .build();
        storage.put(recipe.getId(), updatedRecipe);
        return updatedRecipe;
    }

    @Override
    public Optional<Recipe> getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<Recipe> getByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Recipe name cannot be null");
        }
        return storage.values()
            .stream()
            .filter(recipe -> recipe.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Recipe> getByIngredientType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Type of ingredient cannot be null");
        }
        return storage.values()
            .stream()
            .filter(recipe -> recipe.getIngredients()
                .stream()
                .anyMatch(i -> i.getIngredientType().getType().toLowerCase().contains(type.toLowerCase())))
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!storage.containsKey(id)) {
            throw new DatabaseOperationException("Attempt to delete non-existing recipe.");
        }
        storage.remove(id);
    }

    @Override
    public Collection<Recipe> getAll() {
        return storage.values();
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public boolean exists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return storage.containsKey(id);
    }
}
