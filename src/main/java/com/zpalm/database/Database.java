package com.zpalm.database;

import java.util.Optional;

import com.zpalm.model.Recipe;

public interface Database {

    Recipe add(Recipe recipe);

    void delete(Long id);

    Optional<Recipe> get(Long id);
}
