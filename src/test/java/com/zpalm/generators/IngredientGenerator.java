package com.zpalm.generators;

import com.zpalm.model.Ingredient;
import com.zpalm.model.Unit;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class IngredientGenerator {

    private static Random random = new Random();

    public static Ingredient getRandomIngredient() {
        String ingredientType = RandomStringUtils.randomAlphabetic(5, 10);
        List<Unit> units = Arrays.asList(Unit.values());
        BigDecimal quantity = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50));

        return Ingredient.builder()
            .ingredientType(ingredientType)
            .unit(units.get(random.nextInt(units.size())))
            .quantity(quantity)
            .build();
    }

    public static Ingredient getRandomIngredientWithSpecificType(String type) {
        List<Unit> units = Arrays.asList(Unit.values());
        BigDecimal quantity = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50));

        return Ingredient.builder()
            .ingredientType(type)
            .unit(units.get(random.nextInt(units.size())))
            .quantity(quantity)
            .build();
    }
}
