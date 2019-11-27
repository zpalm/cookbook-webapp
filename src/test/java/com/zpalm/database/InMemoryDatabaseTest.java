package com.zpalm.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zpalm.model.Recipe;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class InMemoryDatabaseTest {

    private Map<Long, Recipe> storage = new HashMap<>();
    private InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(storage);

    @Test
    void shouldAddRecipeToDatabase() {
        Recipe recipeToAdd = Recipe.builder()
            .name("omelette")
            .ingredients(Arrays.asList("eggs", "water", "cheese", "butter"))
            .entry(Arrays.asList(
                "First, beat eggs and water in a small bowl until blended.",
                "Then heat butter on a pan.",
                "Pour the egg mix in.",
                "Gently push cooked portions away from edges.",
                "Sprinkle grated cheese on top."))
            .build();
        Recipe addedRecipe = inMemoryDatabase.add(recipeToAdd);

        assertEquals(1L, (long) addedRecipe.getId());
        assertEquals(storage.get(1L), addedRecipe);
    }

    @Test
    void shouldUpdateRecipeInDatabase() {
        Recipe recipeToAdd = Recipe.builder()
            .id(1L)
            .name("omelette")
            .ingredients(Arrays.asList("eggs", "water", "cheese", "butter"))
            .entry(Arrays.asList(
                "First, beat eggs and water in a small bowl until blended.",
                "Then heat butter on a pan.",
                "Pour the egg mix in.",
                "Gently push cooked portions away from edges.",
                "Sprinkle grated cheese on top."))
            .build();
        storage.put(recipeToAdd.getId(), recipeToAdd);
        Recipe recipeToUpdate = Recipe.builder()
            .id(recipeToAdd.getId())
            .name(recipeToAdd.getName())
            .ingredients(Arrays.asList("eggs", "water", "butter"))
            .entry(recipeToAdd.getEntry())
            .build();

        Recipe updatedRecipe = inMemoryDatabase.add(recipeToUpdate);

        assertEquals(storage.get(recipeToAdd.getId()), updatedRecipe);
    }

    @Test
    void shouldDeleteRecipe() {
        Recipe recipeToAdd = Recipe.builder()
            .name("omelette")
            .ingredients(Arrays.asList("eggs", "water", "cheese", "butter"))
            .entry(Arrays.asList(
                "First, beat eggs and water in a small bowl until blended.",
                "Then heat butter on a pan.",
                "Pour the egg mix in.",
                "Gently push cooked portions away from edges.",
                "Sprinkle grated cheese on top."))
            .build();
        storage.put(recipeToAdd.getId(), recipeToAdd);
        Map<Long, Recipe> expected = new HashMap<>();

        inMemoryDatabase.delete(recipeToAdd.getId());

        assertEquals(expected, storage);
    }
}