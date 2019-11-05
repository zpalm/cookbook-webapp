package com.zpalm.model;

import java.util.List;

public class Recipe {

    private final Long id;
    private final String name;
    private final List<String> ingredients;
    private final List<String> entry;

    public Recipe(Long id, String name, List<String> ingredients, List<String> entry) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.entry = entry;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getEntry() {
        return entry;
    }
}
