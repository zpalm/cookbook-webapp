package com.zpalm.database.sqlmodel;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Table
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final String name;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Ingredient> ingredients;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<RecipeStep> steps;


    public static class RecipeBuilder {
        private Long id;
        private String name;
        private List<Ingredient> ingredients;
        private List<RecipeStep> steps;

        public RecipeBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RecipeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder withIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withSteps(List<RecipeStep> steps) {
            this.steps = steps;
            return this;
        }

        public Recipe build() {
            return new Recipe(id, name, ingredients, steps);
        }
    }
}
