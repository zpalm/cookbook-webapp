package com.zpalm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.zpalm.database.Database;
import com.zpalm.database.DatabaseOperationException;
import com.zpalm.generators.RecipeGenerator;
import com.zpalm.model.Recipe;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    Database database;

    @InjectMocks
    RecipeService service;

    @Test
    void shouldAddRecipeWithNullIdToDatabase() throws ServiceOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithNullId();
        Recipe addedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId() + 1L);
        doReturn(addedRecipe).when(database).save(recipeToAdd);

        Recipe recipe = service.addRecipe(recipeToAdd);

        assertEquals(addedRecipe, recipe);
        verify(database).save(recipeToAdd);
        verify(database, never()).exists(recipeToAdd.getId());
    }

    @Test
    void shouldAddRecipeWithGivenIdToDatabase() throws ServiceOperationException {
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithGivenId(3L);
        Recipe addedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(3L);
        doReturn(false).when(database).exists(recipeToAdd.getId());
        doReturn(addedRecipe).when(database).save(recipeToAdd);

        Recipe recipe = service.addRecipe(recipeToAdd);

        assertEquals(addedRecipe.getId(), recipe.getId());
        assertEquals(addedRecipe, recipe);
        verify(database).save(recipeToAdd);
        verify(database).exists(recipeToAdd.getId());
    }

    @Test
    void addRecipeMethodShouldThrowAnExceptionForNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> service.addRecipe(null));
        verify(database, never()).exists(any());
        verify(database, never()).save(any());
    }

    @Test
    void addRecipeMethodShouldThrowServiceOperationExceptionWhenAddingExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(database).exists(recipeInDatabase.getId());

        assertThrows(ServiceOperationException.class, () -> service.addRecipe(recipeInDatabase));
        verify(database).exists(recipeInDatabase.getId());
        verify(database, never()).save(recipeInDatabase);
    }

    @Test
    void shouldUpdateRecipeInDatabase() throws ServiceOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        Recipe updatedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        doReturn(true).when(database).exists(recipeToUpdate.getId());
        doReturn(updatedRecipe).when(database).save(recipeToUpdate);

        Recipe recipe = service.updateRecipe(recipeToUpdate);

        assertEquals(updatedRecipe, recipe);
        verify(database).save(recipeToUpdate);
        verify(database).exists(recipeToUpdate.getId());
    }

    @Test
    void updateRecipeMethodShouldThrowAnExceptionForNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> service.updateRecipe(null));
        verify(database, never()).exists(any());
        verify(database, never()).save(any());
    }

    @Test
    void updateRecipeMethodShouldThrowServiceOperationExceptionWhenUpdatingNonExistingRecipe() {
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipe();
        doReturn(false).when(database).exists(recipeToUpdate.getId());

        assertThrows(ServiceOperationException.class, () -> service.updateRecipe(recipeToUpdate));
        verify(database).exists(recipeToUpdate.getId());
        verify(database, never()).save(recipeToUpdate);
    }

    @Test
    void shouldDeleteRecipeFromDatabase() throws ServiceOperationException, DatabaseOperationException {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doNothing().when(database).delete(recipeInDatabase.getId());

        service.deleteRecipe(recipeInDatabase.getId());

        verify(database).delete(recipeInDatabase.getId());
    }

    @Test
    void deleteMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteRecipe(null));
    }

    @Test
    void deleteMethodShouldThrowAServiceOperationExceptionForOccurringError() throws DatabaseOperationException {
        Recipe recipeToDelete = RecipeGenerator.getRandomRecipe();
        doThrow(DatabaseOperationException.class).when(database).delete(recipeToDelete.getId());

        assertThrows(ServiceOperationException.class, () -> service.deleteRecipe(recipeToDelete.getId()));
        verify(database).delete(recipeToDelete.getId());
    }

    @Test
    void shouldGetRecipeById() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        doReturn(Optional.of(recipeToGet)).when(database).getById(recipeToGet.getId());

        Optional<Recipe> receivedRecipe = service.getRecipeById(recipeToGet.getId());

        assertTrue(receivedRecipe.isPresent());
        assertEquals(recipeToGet, receivedRecipe.get());
        verify(database).getById(recipeToGet.getId());
    }

    @Test
    void getByIdMethodShouldGetEmptyOptionalForNonExistingRecipe() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        doReturn(Optional.empty()).when(database).getById(recipeToGet.getId());

        Optional<Recipe> receivedRecipe = service.getRecipeById(recipeToGet.getId());

        assertFalse(receivedRecipe.isPresent());
        verify(database).getById(recipeToGet.getId());
    }

    @Test
    void getByIdMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipeById(null));
    }

    @Test
    void shouldGetRecipesByName() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        Collection<Recipe> expected = Collections.singleton(recipeToGet);
        doReturn(expected).when(database).getByName(recipeToGet.getName());

        Collection<Recipe> receivedRecipes = service.getRecipesByName(recipeToGet.getName());

        assertIterableEquals(expected, receivedRecipes);
        verify(database).getByName(recipeToGet.getName());
    }

    @Test
    void getByNameMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipesByName(null));
    }

    @Test
    void shouldGetRecipesByIngredientType() {
        Recipe recipeToGet = RecipeGenerator.getRandomRecipe();
        String ingredientType = recipeToGet.getIngredients().get(1).getIngredientType().getType();
        Collection<Recipe> expected = Collections.singleton(recipeToGet);
        doReturn(expected).when(database).getByIngredientType(ingredientType);

        Collection<Recipe> receivedRecipes = service.getRecipesByIngredientType(ingredientType);

        assertIterableEquals(expected, receivedRecipes);
        verify(database).getByIngredientType(ingredientType);
    }

    @Test
    void getByIngredientTypeMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipesByIngredientType(null));
    }

    @Test
    void shouldGetAllRecipes() {
        Recipe recipe1 = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipe2 = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        Collection<Recipe> expected = Arrays.asList(recipe1, recipe2);
        doReturn(expected).when(database).getAll();

        Collection<Recipe> returnedRecipes = service.getAllRecipes();

        assertEquals(expected, returnedRecipes);
        verify(database).getAll();
    }

    @Test
    void shouldDeleteAllRecipes() {
        doNothing().when(database).deleteAll();

        service.deleteAllRecipes();

        verify(database).deleteAll();
    }

    @Test
    void shouldReturnTrueForExistingRecipe() {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(database).exists(recipeInDatabase.getId());

        assertTrue(service.recipeExists(recipeInDatabase.getId()));
        verify(database).exists(recipeInDatabase.getId());
    }

    @Test
    void shouldReturnFalseForNonExistingRecipe() {
        Recipe nonExistingRecipe = RecipeGenerator.getRandomRecipe();
        doReturn(false).when(database).exists(nonExistingRecipe.getId());

        assertFalse(service.recipeExists(nonExistingRecipe.getId()));
        verify(database).exists(nonExistingRecipe.getId());
    }

    @Test
    void existsMethodShouldThrowAnExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.recipeExists(null));
    }
}
