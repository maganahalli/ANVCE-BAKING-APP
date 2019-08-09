package com.mobile.anvce.baking.api;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.models.Ingredient;

import java.util.List;

public interface RecipesFacade {
    DbRecipe anyRecipe(@NonNull final List<DbRecipe> recipes);

    DbRecipe loadRecipe(int mRecipeId);

    List<DbRecipe> fetchAllRecipes();

    List<Ingredient> getIngredients(DbRecipe recipe);
}
