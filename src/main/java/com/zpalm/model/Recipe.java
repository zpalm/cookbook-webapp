package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonDeserialize(builder = Recipe.RecipeBuilder.class)
@Data
@Builder
public class Recipe {

    private final Long id;
    private final String name;
    private final List<String> ingredients;
    private final List<String> entry;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RecipeBuilder {

    }
}
