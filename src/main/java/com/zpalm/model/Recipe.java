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

    @JsonPOJOBuilder
    public static class RecipeBuilder {
        private Long id;
        private String name;
        private List<String> ingredients;
        private List<String> entry;

        public RecipeBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RecipeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder withIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withEntry(List<String> entry) {
            this.entry = entry;
            return this;
        }

        public Recipe build() {
            return new Recipe(id, name, ingredients,entry);
        }
    }
}
