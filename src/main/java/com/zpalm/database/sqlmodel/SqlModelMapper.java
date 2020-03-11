package com.zpalm.database.sqlmodel;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SqlModelMapper {
    
    @Mapping(target = "id", source = "recipe.id")
    @Mapping(target = "name", source = "recipe.name")
    @Mapping(target = "ingredients", source = "recipe.ingredients")
    @Mapping(target = "steps", source = "recipe.steps")
    Recipe toSqlRecipe(com.zpalm.model.Recipe recipe);

    @Mapping(target = "id", source = "recipe.id")
    @Mapping(target = "name", source = "recipe.name")
    @Mapping(target = "ingredients", source = "recipe.ingredients")
    @Mapping(target = "steps", source = "recipe.steps")
    com.zpalm.model.Recipe toRecipe(Recipe recipe);

    List<com.zpalm.model.Recipe> mapToRecipes(List<Recipe> recipes);

    @Mapping(target = "id", source = "")
    @Mapping(target = "ingredientType", source = "ingredient.ingredientType")
    @Mapping(target = "unit", source = "ingredient.unit")
    @Mapping(target = "quantity", source = "ingredient.quantity")
    Ingredient toSqlIngredient(com.zpalm.model.Ingredient ingredient);

    @Mapping(target = "ingredientType", source = "ingredient.ingredientType")
    @Mapping(target = "unit", source = "ingredient.unit")
    @Mapping(target = "quantity", source = "ingredient.quantity")
    com.zpalm.model.Ingredient toIngredient(Ingredient ingredient);

    @Mapping(target = "id", source = "")
    @Mapping(target = "step", source = "recipeStep.step")
    RecipeStep toSqlRecipeStep(com.zpalm.model.RecipeStep recipeStep);

    @Mapping(target = "step", source = "recipeStep.step")
    com.zpalm.model.RecipeStep toRecipeStep(RecipeStep recipeStep);

    Unit toSqlUnit(com.zpalm.model.Unit unit);

    com.zpalm.model.Unit toUnit(Unit unit);
}
