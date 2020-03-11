package com.zpalm.generators;

import com.zpalm.database.sqlmodel.Ingredient;
import com.zpalm.database.sqlmodel.Unit;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

public class SqlIngredientGenerator {

    private static AtomicLong nextId = new AtomicLong(0);
    private static Random random = new Random();

    public static Ingredient getRandomIngredient() {
        Long id = nextId.incrementAndGet();
        String ingredientType = RandomStringUtils.randomAlphabetic(5, 10);
        List<Unit> units = Arrays.asList(Unit.values());
        BigDecimal quantity = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50));

        return Ingredient.builder()
            .id(id)
            .ingredientType(ingredientType)
            .unit(units.get(random.nextInt(units.size())))
            .quantity(quantity)
            .build();
    }

    public static Ingredient getRandomIngredientWithSpecificType(String type) {
        Long id = nextId.incrementAndGet();
        List<Unit> units = Arrays.asList(Unit.values());
        BigDecimal quantity = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 50));

        return Ingredient.builder()
            .id(id)
            .ingredientType(type)
            .unit(units.get(random.nextInt(units.size())))
            .quantity(quantity)
            .build();
    }
}
