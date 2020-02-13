package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder(builderClassName = "IngredientBuilder", toBuilder = true)
@JsonDeserialize(builder = Ingredient.IngredientBuilder.class)
public class Ingredient {

    //private Long id;
    private IngredientType ingredientType;
    private Unit unit;
    private BigDecimal quantity;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IngredientBuilder {

    }
}
