package com.zpalm.controller;

import com.zpalm.model.Recipe;
import com.zpalm.service.RecipeService;
import com.zpalm.service.ServiceOperationException;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Recipe recipe) throws ServiceOperationException {
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (recipe.getId() != null && recipeService.recipeExists(recipe.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Recipe addedRecipe = recipeService.addRecipe(recipe);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(URI.create(String.format("/recipes/%d", addedRecipe.getId())));
        return new ResponseEntity<>(addedRecipe, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Recipe recipe) throws ServiceOperationException {
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!id.equals(recipe.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!recipeService.recipeExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(recipeService.updateRecipe(recipe), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            if (!recipeService.recipeExists(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            recipeService.deleteRecipe(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceOperationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!recipe.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
    }
}
