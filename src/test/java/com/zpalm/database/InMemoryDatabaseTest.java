package com.zpalm.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zpalm.model.Recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InMemoryDatabaseTest {

    private Map<Long, Recipe> storage = new HashMap<>();
    InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(storage);
    Recipe testRecipe = Recipe.builder()
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

    @Test
    void shouldAddRecipeToDatabase() {

        Recipe recipeToAdd = testRecipe;
        Recipe addedRecipe = inMemoryDatabase.add(recipeToAdd);

        assertEquals(1L, addedRecipe.getId());
        assertEquals(storage.get(1L), addedRecipe);
    }
}