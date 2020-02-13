package com.zpalm.generators;

import com.zpalm.database.sqlmodel.IngredientType;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

public class SqlIngredientTypeGenerator {
    private static AtomicLong nextId = new AtomicLong(0);

    public static IngredientType getRandomIngredientType() {
        Long id = nextId.incrementAndGet();
        String type = RandomStringUtils.randomAlphabetic(5, 10);

        return IngredientType.builder()
            .id(id)
            .type(type)
            .build();
    }
}
