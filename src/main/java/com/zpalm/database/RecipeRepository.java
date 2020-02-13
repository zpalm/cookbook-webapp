package com.zpalm.database;

import com.zpalm.database.sqlmodel.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
