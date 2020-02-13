package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "RecipeBuilder", toBuilder = true)
@JsonDeserialize(builder = Recipe.RecipeBuilder.class)
public class Recipe {

    private Long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RecipeBuilder {

    }
}
