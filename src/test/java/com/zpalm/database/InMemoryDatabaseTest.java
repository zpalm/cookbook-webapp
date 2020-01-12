package com.zpalm.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zpalm.generators.RecipeGenerator;
import com.zpalm.model.Recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class InMemoryDatabaseTest {

    private Map<Long, Recipe> storage = new HashMap<>();
    InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(storage);

    @Test
    void shouldAddRecipeToDatabase() {
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithNullId();
        Recipe addedRecipe = inMemoryDatabase.save(recipeToAdd);

        assertEquals(1L, addedRecipe.getId());
        assertEquals(storage.get(1L), addedRecipe);
    }

    @Test
    void shouldUpdateRecipeInDatabase() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        Recipe updatedRecipe = inMemoryDatabase.save(recipeToUpdate);

        assertEquals(storage.get(recipeInDatabase.getId()), updatedRecipe);
    }

    @Test
    void saveMethodShouldThrowAnExceptionForNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.save(null));
    }

    @Test
    void shouldDeleteRecipeFromDatabase() throws DatabaseOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        inMemoryDatabase.delete(recipeInDatabase.getId());

        assertEquals(Collections.EMPTY_MAP, storage);
    }

    @Test
    void deleteMethodShouldThrowAnExceptionWhenDeletingNonExistingRecipe() {
        assertThrows(DatabaseOperationException.class, () -> inMemoryDatabase.delete(1L));
    }

    @Test
    void deleteMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.delete(null));
    }

    @Test
    void shouldGetRecipeById() throws DatabaseOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);
        storage.put(recipeToGet.getId(), recipeToGet);

        Optional<Recipe> receivedRecipe = inMemoryDatabase.getById(recipeToGet.getId());

        assertTrue(receivedRecipe.isPresent());
        assertEquals(recipeToGet, receivedRecipe.get());
    }

    @Test
    void getByIdMethodShouldGetEmptyOptionalForNonExistingRecipe() throws DatabaseOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        Optional<Recipe> receivedRecipe = inMemoryDatabase.getById(recipeToGet.getId());

        assertFalse(receivedRecipe.isPresent());
    }

    @Test
    void getByIdMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.getById(null));
    }

    @Test
    void shouldGetAllRecipes() {
        Recipe recipe1 = RecipeGenerator.getRandomRecipe();
        Recipe recipe2 = RecipeGenerator.getRandomRecipe();
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);
        Collection<Recipe> expected = storage.values();

        Collection<Recipe> returnedRecipes = inMemoryDatabase.getAll();

        assertEquals(expected, returnedRecipes);
    }

    @Test
    void shouldDeleteAllRecipes() {
        Recipe recipe1 = RecipeGenerator.getRandomRecipe();
        Recipe recipe2 = RecipeGenerator.getRandomRecipe();
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);

        inMemoryDatabase.deleteAll();

        assertEquals(new HashMap<>(), storage);
    }

    @Test
    void shouldReturnTrueForExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        assertTrue(inMemoryDatabase.exists(recipeInDatabase.getId()));
    }

    @Test
    void shouldReturnFalseForNonExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        assertFalse(inMemoryDatabase.exists(recipeInDatabase.getId() + 1));
    }

    @Test
    void existsMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.exists(null));
    }
}
