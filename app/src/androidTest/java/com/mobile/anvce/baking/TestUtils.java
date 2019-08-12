package com.mobile.anvce.baking;

import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.transformers.RecipeFromDbRecipe;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static final String TAG = TestUtils.class.getSimpleName();
    private static AppDatabase recipeDataBase;
    public TestUtils(final AppDatabase recipeDataBase) {
        this.recipeDataBase = recipeDataBase;
    }

    public static Recipe getRecipeByPosition(final int position) {
        final List<Recipe> recipes = loadRecipes();
        final int minimum_size = position + 1;
        return (recipes.size() >= minimum_size) ? recipes.get(position) : new Recipe();
    }

    private static List<Recipe> loadRecipes() {
        final List<Recipe> recipes = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<DbRecipe> recipeList = recipeDataBase.receipeDao().fetchAllRecipes();
            if (recipeList != null) {
                for (DbRecipe dRecipe : recipeList) {
                    Recipe recipe = new RecipeFromDbRecipe().transform(dRecipe);
                    recipes.add(recipe);
                }
            }
        });

        return recipes;

    }

}
