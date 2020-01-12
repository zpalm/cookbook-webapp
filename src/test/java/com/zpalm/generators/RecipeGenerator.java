package com.zpalm.generators;

import com.zpalm.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class RecipeGenerator {

    public static Recipe getRandomRecipe() {
        Long id = ThreadLocalRandom.current().nextLong(11);
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<String> ingredients = new ArrayList<>();
        List<String> entry = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(RandomStringUtils.randomAlphabetic(5, 10));
            entry.add(RandomStringUtils.randomAlphabetic(5, 10));
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .entry(entry)
            .build();
    }

    public static Recipe getRandomRecipeWithNullId() {
        Long id = null;
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<String> ingredients = new ArrayList<>();
        List<String> entry = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(RandomStringUtils.randomAlphabetic(5, 10));
            entry.add(RandomStringUtils.randomAlphabetic(5, 10));
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .entry(entry)
            .build();
    }

    public static Recipe getRandomRecipeWithGivenId(Long id) {
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        List<String> ingredients = new ArrayList<>();
        List<String> entry = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ingredients.add(RandomStringUtils.randomAlphabetic(5, 10));
            entry.add(RandomStringUtils.randomAlphabetic(5, 10));
        }

        return Recipe.builder()
            .id(id)
            .name(name)
            .ingredients(ingredients)
            .entry(entry)
            .build();
    }
}
