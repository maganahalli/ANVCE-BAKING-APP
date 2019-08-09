package com.mobile.anvce.baking.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of RecipesFacade interface
 *
 * @author Venkatesh Maganahalli.
 */
public class BaseRecipesFacade implements BakingAppConstants, RecipesFacade {

    private static final String TAG = BaseRecipesFacade.class.getSimpleName();

    private static final Random RANDOM_GENERATOR = new Random();
    private final Context context;
    private AppDatabase recipeDataBase;
    private DbRecipe recipe;

    public BaseRecipesFacade(Context context) {
        this.context = context;
        recipeDataBase = AppDatabase.getInstance(context);
    }

    @Override
    public DbRecipe anyRecipe(@NonNull final List<DbRecipe> recipes) {
        return recipes.get(RANDOM_GENERATOR.nextInt(recipes.size()));
    }

    @Override
    public DbRecipe loadRecipe(int mRecipeId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            final DbRecipe retrievedRecipe = recipeDataBase.receipeDao().retrieveRecipeById(mRecipeId);
            recipe = retrievedRecipe;

        });

        return recipe;

    }

    @Override
    public List<DbRecipe> fetchAllRecipes() {
        List<DbRecipe> recipes = new ArrayList<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<DbRecipe> retrievedRecipes = recipeDataBase.receipeDao().fetchAllRecipes();
            assert retrievedRecipes != null;
            recipes.addAll(retrievedRecipes);
        });
        return recipes;

    }

    @Override
    public List<Ingredient> getIngredients(@NonNull DbRecipe recipe) {
        assert recipe.getIngredientsListAsString() != null;
        final List<Ingredient> ingredientList = new RecipeCustomDataConverter().toIngredientList(recipe.getIngredientsListAsString());
        return ingredientList;
    }
}
