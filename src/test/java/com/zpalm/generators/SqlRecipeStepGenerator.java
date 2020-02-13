package com.zpalm.generators;

import com.zpalm.database.sqlmodel.RecipeStep;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

public class SqlRecipeStepGenerator {
    private static AtomicLong nextId = new AtomicLong(0);

    public static RecipeStep getRandomRecipeStep() {
        Long id = nextId.incrementAndGet();
        String step = RandomStringUtils.randomAlphabetic(5, 10);

        return RecipeStep.builder()
            .id(id)
            .step(step)
            .build();
    }
}
