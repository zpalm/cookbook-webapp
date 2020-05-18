package com.zpalm.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.zpalm.database.sqlmodel.SqlModelMapper;
import com.zpalm.database.sqlmodel.SqlModelMapperImpl;
import com.zpalm.generators.RecipeGenerator;
import com.zpalm.generators.SqlRecipeGenerator;
import com.zpalm.model.Recipe;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HibernateDatabaseTest {
    @Mock
    RecipeRepository recipeRepository;
    SqlModelMapper sqlModelMapper = new SqlModelMapperImpl();
    HibernateDatabase hibernateDatabase;

    @BeforeEach
    void setUp() {
        hibernateDatabase = new HibernateDatabase(recipeRepository, sqlModelMapper);
    }

    @Test
    void shouldSaveRecipe() {
        Recipe recipeToSave = RecipeGenerator.getRandomRecipe();
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipeToSave);
        doReturn(sqlRecipe).when(recipeRepository).save(sqlRecipe);

        Recipe savedRecipe = hibernateDatabase.save(recipeToSave);

        assertEquals(recipeToSave, savedRecipe);
        verify(recipeRepository).save(sqlRecipe);
    }

    @Test
    void saveMethodShouldThrowAnExceptionForNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.save(null));
    }

    @Test
    void shouldDeleteRecipeFromDatabase() throws DatabaseOperationException {
        doReturn(true).when(recipeRepository).existsById(1L);
        doNothing().when(recipeRepository).deleteById(1L);

        hibernateDatabase.delete(1L);

        verify(recipeRepository).existsById(1L);
        verify(recipeRepository).deleteById(1L);
    }

    @Test
    void deleteMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.delete(null));
    }

    @Test
    void deleteMethodShouldThrowAnExceptionWhenDeletingNonExistingRecipe() {
        doReturn(false).when(recipeRepository).existsById(1L);

        assertThrows(DatabaseOperationException.class, () -> hibernateDatabase.delete(1L));

        verify(recipeRepository).existsById(1L);
    }

    @Test
    void shouldGetRecipeById() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipeInDatabase);
        doReturn(Optional.of(sqlRecipe)).when(recipeRepository).findById(recipeInDatabase.getId());

        Optional<Recipe> receivedRecipe = hibernateDatabase.getById(recipeInDatabase.getId());

        assertTrue(receivedRecipe.isPresent());
        assertEquals(recipeInDatabase, receivedRecipe.get());
        verify(recipeRepository).findById(recipeInDatabase.getId());
    }

    @Test
    void getByIdMethodShouldGetEmptyOptionalForNonExistingRecipe() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        doReturn(Optional.empty()).when(recipeRepository).findById(recipeToGet.getId());

        Optional<Recipe> receivedRecipe = hibernateDatabase.getById(recipeToGet.getId());

        assertFalse(receivedRecipe.isPresent());
        verify(recipeRepository).findById(recipeToGet.getId());
    }

    @Test
    void getByIdMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.getById(null));
    }

    @Test
    void shouldGetRecipesByName() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipeToGet);
        List<com.zpalm.database.sqlmodel.Recipe> sqlRecipes = List.of(sqlRecipe);
        List<Recipe> expected = sqlModelMapper.mapToRecipes(sqlRecipes);
        doReturn(sqlRecipes).when(recipeRepository).findByNameContainingIgnoreCase(recipeToGet.getName());

        Collection<Recipe> receivedRecipes = hibernateDatabase.getByName(recipeToGet.getName());

        assertIterableEquals(expected, receivedRecipes);
        verify(recipeRepository).findByNameContainingIgnoreCase(recipeToGet.getName());
    }

    @Test
    void getByNameMethodShouldThrowAnExceptionForNullName() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.getByName(null));
    }

    @Test
    void shouldGetRecipesByIngredientType() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipeToGet);
        String ingredientType = recipeToGet.getIngredients().get(1).getIngredientType();
        List<com.zpalm.database.sqlmodel.Recipe> sqlRecipes = List.of(sqlRecipe);
        List<Recipe> expected = sqlModelMapper.mapToRecipes(sqlRecipes);
        doReturn(sqlRecipes).when(recipeRepository).findByIngredientsIngredientTypeContainingIgnoreCase(ingredientType);

        Collection<Recipe> receivedRecipes = hibernateDatabase.getByIngredientType(ingredientType);

        assertIterableEquals(expected, receivedRecipes);
        verify(recipeRepository).findByIngredientsIngredientTypeContainingIgnoreCase(ingredientType);
    }

    @Test
    void getByIngredientTypeMethodShouldThrowAnExceptionForNullType() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.getByIngredientType(null));
    }

    @Test
    void shouldGetAllRecipes() {
        com.zpalm.database.sqlmodel.Recipe recipe1 = SqlRecipeGenerator.getRandomRecipe();
        com.zpalm.database.sqlmodel.Recipe recipe2 = SqlRecipeGenerator.getRandomRecipe();
        List<com.zpalm.database.sqlmodel.Recipe> sqlRecipes = Arrays.asList(recipe1, recipe2);
        List<Recipe> expected = sqlModelMapper.mapToRecipes(sqlRecipes);
        doReturn(sqlRecipes).when(recipeRepository).findAll();

        Collection<Recipe> receivedRecipes = hibernateDatabase.getAll();

        assertEquals(expected, receivedRecipes);
        verify(recipeRepository).findAll();
    }

    @Test
    void shouldDeleteAllRecipes() {
        doNothing().when(recipeRepository).deleteAll();

        hibernateDatabase.deleteAll();

        verify(recipeRepository).deleteAll();
    }

    @Test
    void shouldReturnTrueForExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(recipeRepository).existsById(recipeInDatabase.getId());

        assertTrue(hibernateDatabase.exists(recipeInDatabase.getId()));
        verify(recipeRepository).existsById(recipeInDatabase.getId());
    }

    @Test
    void shouldReturnFalseForNonExistingRecipe() {
        Recipe nonExistingRecipe = RecipeGenerator.getRandomRecipe();
        doReturn(false).when(recipeRepository).existsById(nonExistingRecipe.getId());

        assertFalse(hibernateDatabase.exists(nonExistingRecipe.getId()));
        verify(recipeRepository).existsById(nonExistingRecipe.getId());
    }

    @Test
    void existsMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.exists(null));
    }

}
