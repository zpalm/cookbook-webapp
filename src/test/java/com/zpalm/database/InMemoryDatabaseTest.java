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

    @Test
    void shouldAddRecipeToDatabase() {
        Recipe recipeToAdd = new Recipe(1L, "omelette", Arrays.asList("eggs", "water", "cheese", "butter"), Arrays.asList(
            "First, beat eggs and water in a small bowl until blended.",
            "Then heat butter on a pan.",
            "Pour the egg mix in.",
            "Gently push cooked portions away from edges.",
            "Sprinkle grated cheese on top."));
        Recipe addedRecipe = inMemoryDatabase.add(recipeToAdd);

        assertEquals(1L, addedRecipe.getId());
        assertEquals(storage.get(1L), addedRecipe);
    }
}