package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "IngredientBuilder", toBuilder = true)
@JsonDeserialize(builder = Ingredient.IngredientBuilder.class)
public class Ingredient {

    private IngredientType ingredientType;
    private Unit unit;
    private BigDecimal quantity;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IngredientBuilder {

    }
}
