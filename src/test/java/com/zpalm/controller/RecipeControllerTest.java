package com.zpalm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zpalm.generators.RecipeGenerator;
import com.zpalm.model.Recipe;
import com.zpalm.service.RecipeService;
import com.zpalm.service.ServiceOperationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @MockBean
    RecipeService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldAddRecipeWithNullIdToDatabase() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithNullId();
        Recipe addedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId() + 1L);
        doReturn(addedRecipe).when(service).addRecipe(recipeToAdd);

        mockMvc.perform(post("/recipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("location", String.format("/recipes/%d", addedRecipe.getId())))
            .andExpect(content().json(objectMapper.writeValueAsString(addedRecipe)));

        verify(service).addRecipe(recipeToAdd);
        verify(service, never()).recipeExists(recipeToAdd.getId());
    }

    @Test
    void shouldAddRecipeWithGivenIdToDatabase() throws Exception {
        Recipe recipeToAdd = RecipeGenerator.getRandomRecipeWithGivenId(3L);
        Recipe addedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(3L);
        doReturn(false).when(service).recipeExists(recipeToAdd.getId());
        doReturn(addedRecipe).when(service).addRecipe(recipeToAdd);

        mockMvc.perform(post("/recipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("location", String.format("/recipes/%d", addedRecipe.getId())))
            .andExpect(content().json(objectMapper.writeValueAsString(addedRecipe)));

        verify(service).addRecipe(recipeToAdd);
        verify(service).recipeExists(recipeToAdd.getId());
    }

    @Test
    void addRecipeMethodShouldReturnBadRequestStatusForNullRecipe() throws Exception {
        mockMvc.perform(post("/recipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).addRecipe(any());
        verify(service, never()).recipeExists(any());
    }

    @Test
    void addRecipeMethodShouldReturnConflictStatusWhenAddingExistingRecipe() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(service).recipeExists(recipeInDatabase.getId());

        mockMvc.perform(post("/recipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeInDatabase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());

        verify(service, never()).addRecipe(recipeInDatabase);
        verify(service).recipeExists(recipeInDatabase.getId());
    }

    @Test
    void shouldUpdateRecipeInDatabase() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        Recipe updatedRecipe = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        doReturn(true).when(service).recipeExists(recipeToUpdate.getId());
        doReturn(updatedRecipe).when(service).updateRecipe(recipeToUpdate);

        mockMvc.perform(put(String.format("/recipes/%d", recipeToUpdate.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeToUpdate))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(updatedRecipe)));

        verify(service).updateRecipe(recipeToUpdate);
        verify(service).recipeExists(recipeToUpdate.getId());
    }

    @Test
    void updateRecipeMethodShouldReturnBadRequestStatusForNullRecipe() throws Exception {
        mockMvc.perform(put(("/recipes/1"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).recipeExists(any());
        verify(service, never()).updateRecipe(any());
    }

    @Test
    void updateRecipeMethodShouldReturnBadRequestStatusWhenUpdatingRecipeWithNonMatchingId() throws Exception {
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipe();

        mockMvc.perform(put((String.format("/recipes/%d", (recipeToUpdate.getId() + 1))))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeToUpdate))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).recipeExists(any());
        verify(service, never()).updateRecipe(any());
    }

    @Test
    void updateRecipeMethodShouldReturnNotFoundStatusForNonExistingRecipe() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        Recipe recipeToUpdate = RecipeGenerator.getRandomRecipeWithGivenId(recipeInDatabase.getId());
        doReturn(false).when(service).recipeExists(recipeToUpdate.getId());

        mockMvc.perform(put((String.format("/recipes/%d", recipeToUpdate.getId())))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(recipeToUpdate))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).recipeExists(recipeToUpdate.getId());
        verify(service, never()).updateRecipe(any());
    }

    @Test
    void shouldDeleteRecipeFromDatabase() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(service).recipeExists(recipeInDatabase.getId());
        doNothing().when(service).deleteRecipe(recipeInDatabase.getId());

        mockMvc.perform(delete((String.format("/recipes/%d", recipeInDatabase.getId())))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(service).recipeExists(recipeInDatabase.getId());
        verify(service).deleteRecipe(recipeInDatabase.getId());
    }

    @Test
    void deleteMethodShouldReturnNotFoundStatusForNonExistingRecipe() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(false).when(service).recipeExists(recipeInDatabase.getId());

        mockMvc.perform(delete((String.format("/recipes/%d", recipeInDatabase.getId())))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).recipeExists(recipeInDatabase.getId());
        verify(service, never()).deleteRecipe(recipeInDatabase.getId());
    }

    @Test
    void deleteMethodShouldReturnInternalSererErrorWhenAServiceOperationExceptionOccurs() throws Exception {
        Recipe recipeToDelete = RecipeGenerator.getRandomRecipe();
        doReturn(true).when(service).recipeExists(recipeToDelete.getId());
        doThrow(ServiceOperationException.class).when(service).deleteRecipe(recipeToDelete.getId());

        mockMvc.perform(delete(String.format("/recipes/%d", recipeToDelete.getId()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(service).recipeExists(recipeToDelete.getId());
        verify(service).deleteRecipe(recipeToDelete.getId());
    }

    @Test
    void shouldGetRecipeById() throws Exception {
        Recipe recipeInDatabase = RecipeGenerator.getRandomRecipe();
        doReturn(Optional.of(recipeInDatabase)).when(service).getRecipeById(recipeInDatabase.getId());

        mockMvc.perform(get(String.format("/recipes/%d", recipeInDatabase.getId()))
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(recipeInDatabase)));

        verify(service).getRecipeById(recipeInDatabase.getId());
    }

    @Test
    void getRecipeByIdMethodShouldReturnNotFoundStatusWhenGettingNonExistingRecipe() throws Exception {
        Recipe nonExistingRecipe = RecipeGenerator.getRandomRecipe();
        doReturn(Optional.empty()).when(service).getRecipeById(nonExistingRecipe.getId());

        mockMvc.perform(get(String.format("/recipes/%d", nonExistingRecipe.getId()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).getRecipeById(nonExistingRecipe.getId());
    }

    @Test
    void shouldGetRecipesByName() throws Exception {
        Recipe recipe = RecipeGenerator.getRandomRecipe();
        Collection<Recipe> receivedRecipes = Collections.singleton(recipe);
        doReturn(receivedRecipes).when(service).getRecipesByName(recipe.getName());

        mockMvc.perform(get(String.format("/recipes/byName?name=%s", recipe.getName()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(receivedRecipes)));

        verify(service).getRecipesByName(recipe.getName());
    }

    @Test
    void getRecipeByNameShouldReturnBadRequestStatusForNullName() throws Exception {
        mockMvc.perform(get("/recipes/byName")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).getRecipesByName(any());
    }

    @Test
    void shouldGetRecipesByIngredientType() throws Exception {
        Recipe recipe = RecipeGenerator.getRandomRecipe();
        String ingredientType = recipe.getIngredients().get(1).getIngredientType();
        Collection<Recipe> receivedRecipes = Collections.singleton(recipe);
        doReturn(receivedRecipes).when(service).getRecipesByIngredientType(ingredientType);

        mockMvc.perform(get(String.format("/recipes/byIngredientType?type=%s", ingredientType))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(receivedRecipes)));

        verify(service).getRecipesByIngredientType(ingredientType);
    }

    @Test
    void getRecipeByIngredientTypeShouldReturnBadRequestStatusForNullType() throws Exception {
        mockMvc.perform(get("/recipes/byIngredientType")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, never()).getRecipesByIngredientType(any());
    }

    @Test
    void shouldGetAllRecipes() throws Exception {
        Recipe recipe1 = RecipeGenerator.getRandomRecipeWithGivenId(1L);
        Recipe recipe2 = RecipeGenerator.getRandomRecipeWithGivenId(2L);
        Collection<Recipe> receivedRecipes = Arrays.asList(recipe1, recipe2);
        doReturn(receivedRecipes).when(service).getAllRecipes();

        mockMvc.perform(get("/recipes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(receivedRecipes)));

        verify(service).getAllRecipes();
    }
}
