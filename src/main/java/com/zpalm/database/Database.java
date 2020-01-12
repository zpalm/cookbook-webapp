package com.zpalm.database;

import com.zpalm.model.Recipe;

import java.util.Collection;
import java.util.Optional;

public interface Database {

    Recipe save(Recipe recipe);

    void delete(Long id) throws DatabaseOperationException;

    Optional<Recipe> getById(Long id);

    Collection<Recipe> getAll();

    void deleteAll();

    boolean exists(Long id);
}
