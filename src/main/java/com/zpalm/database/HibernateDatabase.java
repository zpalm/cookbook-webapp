package com.zpalm.database;

import com.zpalm.database.sqlmodel.SqlModelMapper;
import com.zpalm.model.Recipe;
import java.util.Collection;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "com.zpalm.database", havingValue = "hibernate")
public class HibernateDatabase implements Database {

    private final RecipeRepository recipeRepository;
    private SqlModelMapper sqlModelMapper;

    public HibernateDatabase(RecipeRepository recipeRepository, SqlModelMapper sqlModelMapper) {
        this.recipeRepository = recipeRepository;
        this.sqlModelMapper = sqlModelMapper;
    }

    @Override
    public Recipe save(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipe);
        return sqlModelMapper.toRecipe(recipeRepository.save(sqlRecipe));
    }

    @Override
    public void delete(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!recipeRepository.existsById(id)) {
            throw new DatabaseOperationException("Attempt to delete non-existing recipe.");
        }
        recipeRepository.deleteById(id);
    }

    @Override
    public Optional<Recipe> getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Optional<com.zpalm.database.sqlmodel.Recipe> foundRecipe = recipeRepository.findById(id);
        return foundRecipe.map(recipe -> sqlModelMapper.toRecipe(recipe));
    }

    @Override
    public Collection<Recipe> getAll() {
        return sqlModelMapper.mapToRecipes(recipeRepository.findAll());
    }

    @Override
    public void deleteAll() {
        recipeRepository.deleteAll();
    }

    @Override
    public boolean exists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return recipeRepository.existsById(id);
    }
}
