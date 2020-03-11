package com.zpalm.database;

import com.zpalm.database.sqlmodel.Recipe;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    List<Recipe> findByNameContainingIgnoreCase(String name);

    List<Recipe> findByIngredientsIngredientTypeIgnoreCase(String type);
}
