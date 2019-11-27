package com.zpalm.database;

import com.zpalm.model.Recipe;

import java.util.Optional;

public interface Database {

    Recipe add(Recipe recipe);

    void delete(Long id);

    Optional<Recipe> getById(Long id);
}
