package com.zpalm.generators;

import com.zpalm.database.sqlmodel.Ingredient;
import com.zpalm.database.sqlmodel.Recipe;
import com.zpalm.database.sqlmodel.RecipeStep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

public class SqlRecipeGenerator {
    private static AtomicLong nextId = new AtomicLong(0);

    public static Recipe getRandomRecipe() {
        Long id = nextId.incrementAndGet();
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<Ingredient> ingredients = new ArrayList<>();
        List<RecipeStep> steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(SqlIngredientGenerator.getRandomIngredient());
            steps.add(SqlRecipeStepGenerator.getRandomRecipeStep());
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .steps(steps)
            .build();
    }

    public static Recipe getRandomRecipeWithNullId() {
        Long id = null;
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<Ingredient> ingredients = new ArrayList<>();
        List<RecipeStep> steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(SqlIngredientGenerator.getRandomIngredient());
            steps.add(SqlRecipeStepGenerator.getRandomRecipeStep());
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .steps(steps)
            .build();
    }

    public static Recipe getRandomRecipeWithGivenId(Long id) {
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<Ingredient> ingredients = new ArrayList<>();
        List<RecipeStep> steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(SqlIngredientGenerator.getRandomIngredient());
            steps.add(SqlRecipeStepGenerator.getRandomRecipeStep());
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .steps(steps)
            .build();
    }
}
