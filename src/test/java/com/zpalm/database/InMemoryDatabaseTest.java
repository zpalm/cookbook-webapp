package com.zpalm.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zpalm.generators.IngredientGenerator;
import com.zpalm.generators.RecipeGenerator;
import com.zpalm.generators.RecipeStepGenerator;
import com.zpalm.model.Recipe;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class InMemoryDatabaseTest {

    private Map<Long, Recipe> storage = new HashMap<>();
    InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(storage);

    @Test
    void shouldAddRecipeWithNullIdToDatabase() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithNullId();
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        Recipe addedRecipe = inMemoryDatabase.save(recipeToAdd);

        assertEquals((recipeInDatabase.getId() + 1L), addedRecipe.getId());
        assertEquals(storage.get((recipeInDatabase.getId() + 1L)), addedRecipe);
    }

    @Test
    void shouldAddRecipeWithGivenIdToDatabase() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithGivenId(3L);
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        Recipe addedRecipe = inMemoryDatabase.save(recipeToAdd);

        assertEquals(recipeToAdd.getId(), addedRecipe.getId());
        assertEquals(storage.get(recipeToAdd.getId()), addedRecipe);
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
    void shouldGetRecipeById() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToGet = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        storage.put(recipeInDatabase.getId(), recipeInDatabase);
        storage.put(recipeToGet.getId(), recipeToGet);

        Optional<Recipe> receivedRecipe = inMemoryDatabase.getById(recipeToGet.getId());

        assertTrue(receivedRecipe.isPresent());
        assertEquals(recipeToGet, receivedRecipe.get());
    }

    @Test
    void getByIdMethodShouldReturnEmptyOptionalForNonExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToGet = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        storage.put(recipeInDatabase.getId(), recipeInDatabase);

        Optional<Recipe> receivedRecipe = inMemoryDatabase.getById(recipeToGet.getId());

        assertFalse(receivedRecipe.isPresent());
    }

    @Test
    void getByIdMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.getById(null));
    }

    @Test
    void shouldGetRecipeByName() {
        Recipe recipe1 = Recipe.builder()
            .id(1L)
            .name("Omelette")
            .steps(List.of(RecipeStepGenerator.getRandomRecipeStep(), RecipeStepGenerator.getRandomRecipeStep()))
            .ingredients(List.of(IngredientGenerator.getRandomIngredient(), IngredientGenerator.getRandomIngredient()))
            .build();
        Recipe recipe2 = Recipe.builder()
            .id(2L)
            .name("Tomato soup")
            .steps(List.of(RecipeStepGenerator.getRandomRecipeStep(), RecipeStepGenerator.getRandomRecipeStep()))
            .ingredients(List.of(IngredientGenerator.getRandomIngredient(), IngredientGenerator.getRandomIngredient()))
            .build();
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);
        Collection<Recipe> expected = Collections.singleton(recipe1);

        Collection<Recipe> receivedRecipes = inMemoryDatabase.getByName("omelette");

        assertIterableEquals(expected, receivedRecipes);
    }

    @Test
    void getByNameMethodShouldThrowAnExceptionForNullName() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.getByName(null));
    }

    @Test
    void shouldGetRecipesByIngredientType() {
        Recipe recipe1 = Recipe.builder()
            .id(1L)
            .name("Omelette")
            .steps(List.of(RecipeStepGenerator.getRandomRecipeStep(), RecipeStepGenerator.getRandomRecipeStep()))
            .ingredients(List.of(IngredientGenerator.getRandomIngredientWithSpecificType("eggs"), IngredientGenerator.getRandomIngredientWithSpecificType("milk")))
            .build();
        Recipe recipe2 = Recipe.builder()
            .id(2L)
            .name("Toasts")
            .steps(List.of(RecipeStepGenerator.getRandomRecipeStep(), RecipeStepGenerator.getRandomRecipeStep()))
            .ingredients(List.of(IngredientGenerator.getRandomIngredientWithSpecificType("bread"), IngredientGenerator.getRandomIngredientWithSpecificType("ham")))
            .build();
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);
        Collection<Recipe> expected = Collections.singleton(recipe2);

        Collection<Recipe> receivedRecipes = inMemoryDatabase.getByIngredientType("ham");

        assertIterableEquals(expected, receivedRecipes);
    }

    @Test
    void getByIngredientTypeMethodShouldThrowAnExceptionForNullType() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryDatabase.getByIngredientType(null));
    }

    @Test
    void shouldGetAllRecipes() {
        Recipe recipe1 = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipe2 = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);
        Collection<Recipe> expected = storage.values();

        Collection<Recipe> receivedRecipes = inMemoryDatabase.getAll();

        assertIterableEquals(expected, receivedRecipes);
    }

    @Test
    void shouldDeleteAllRecipes() {
        Recipe recipe1 = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipe2 = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        storage.put(recipe1.getId(), recipe1);
        storage.put(recipe2.getId(), recipe2);

        inMemoryDatabase.deleteAll();

        assertEquals(new HashMap<Long, Recipe>(), storage);
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
