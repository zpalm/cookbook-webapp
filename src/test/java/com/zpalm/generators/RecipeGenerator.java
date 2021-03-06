package com.zpalm.generators;

import com.zpalm.model.Ingredient;
import com.zpalm.model.Recipe;
import com.zpalm.model.RecipeStep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

public class RecipeGenerator {
    private static AtomicLong nextId = new AtomicLong(0);

    public static Recipe getRandomRecipe() {
        Long id = nextId.incrementAndGet();
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<Ingredient> ingredients = new ArrayList<>();
        List<RecipeStep> steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(IngredientGenerator.getRandomIngredient());
            steps.add(RecipeStepGenerator.getRandomRecipeStep());
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
            ingredients.add(IngredientGenerator.getRandomIngredient());
            steps.add(RecipeStepGenerator.getRandomRecipeStep());
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
            ingredients.add(IngredientGenerator.getRandomIngredient());
            steps.add(RecipeStepGenerator.getRandomRecipeStep());
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .steps(steps)
            .build();
    }
}
