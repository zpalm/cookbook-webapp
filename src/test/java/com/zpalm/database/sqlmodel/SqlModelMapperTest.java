package com.zpalm.database.sqlmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zpalm.generators.IngredientGenerator;
import com.zpalm.generators.RecipeGenerator;
import com.zpalm.generators.RecipeStepGenerator;
import com.zpalm.generators.SqlIngredientGenerator;
import com.zpalm.generators.SqlRecipeGenerator;
import com.zpalm.generators.SqlRecipeStepGenerator;
import com.zpalm.model.Ingredient;
import com.zpalm.model.Recipe;
import com.zpalm.model.RecipeStep;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class SqlModelMapperTest {
    private SqlModelMapper sqlModelMapper = Mappers.getMapper(SqlModelMapper.class);

    @Test
    void shouldMapRecipeToSqlRecipe() {
        Recipe recipe = RecipeGenerator.getRandomRecipe();

        com.zpalm.database.sqlmodel.Recipe sqlRecipe = sqlModelMapper.toSqlRecipe(recipe);

        assertEquals(recipe.getId(), sqlRecipe.getId());
        assertEquals(recipe.getName(), sqlRecipe.getName());
    }

    @Test
    void shouldMapSqlRecipeToRecipe() {
        com.zpalm.database.sqlmodel.Recipe sqlRecipe = SqlRecipeGenerator.getRandomRecipe();

        Recipe recipe = sqlModelMapper.toRecipe(sqlRecipe);

        assertEquals(sqlRecipe.getId(), recipe.getId());
        assertEquals(sqlRecipe.getName(), recipe.getName());
    }

    @Test
    void shouldMapIngredientToSqlIngredient() {
        Ingredient ingredient = IngredientGenerator.getRandomIngredient();

        com.zpalm.database.sqlmodel.Ingredient sqlIngredient = sqlModelMapper.toSqlIngredient(ingredient);

        assertEquals(ingredient.getQuantity(), sqlIngredient.getQuantity());
    }

    @Test
    void shouldMapSqlIngredientToIngredient() {
        com.zpalm.database.sqlmodel.Ingredient sqlIngredient = SqlIngredientGenerator.getRandomIngredient();

        Ingredient ingredient = sqlModelMapper.toIngredient(sqlIngredient);

        assertEquals(sqlIngredient.getQuantity(), ingredient.getQuantity());
    }

    @Test
    void shouldMapRecipeStepToSqlRecipeStep() {
        RecipeStep recipeStep = RecipeStepGenerator.getRandomRecipeStep();

        com.zpalm.database.sqlmodel.RecipeStep sqlRecipeStep = sqlModelMapper.toSqlRecipeStep(recipeStep);

        assertEquals(recipeStep.getStep(), sqlRecipeStep.getStep());
    }

    @Test
    void shouldMapSqlRecipeStepToRecipeStep() {
        com.zpalm.database.sqlmodel.RecipeStep sqlRecipeStep = SqlRecipeStepGenerator.getRandomRecipeStep();

        RecipeStep recipeStep = sqlModelMapper.toRecipeStep(sqlRecipeStep);

        assertEquals(sqlRecipeStep.getStep(), recipeStep.getStep());
    }
}
