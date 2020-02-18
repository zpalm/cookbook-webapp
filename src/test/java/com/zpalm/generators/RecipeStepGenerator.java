package com.zpalm.generators;

import com.zpalm.model.RecipeStep;

import org.apache.commons.lang3.RandomStringUtils;

public class RecipeStepGenerator {
    public static RecipeStep getRandomRecipeStep() {
        String step = RandomStringUtils.randomAlphabetic(5, 10);

        return RecipeStep.builder()
            .step(step)
            .build();
    }
}
