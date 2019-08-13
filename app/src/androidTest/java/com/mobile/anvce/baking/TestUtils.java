package com.mobile.anvce.baking;

import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.utilities.RecipeDatabaseUtil;

import java.util.List;

public class TestUtils {

    public static final String TAG = TestUtils.class.getSimpleName();
    private static AppDatabase recipeDataBase;
    private static RecipeDatabaseUtil recipeDatabaseUtil;

    public TestUtils(final AppDatabase recipeDataBase, final RecipeDatabaseUtil recipeDatabaseUtil) {
        this.recipeDataBase = recipeDataBase;
        this.recipeDatabaseUtil = recipeDatabaseUtil;
    }

    public static Recipe getRecipeByPosition(final int position) {
        final List<Recipe> recipes = loadRecipes();
        final int minimum_size = position + 1;
        return (recipes.size() >= minimum_size) ? recipes.get(position) : new Recipe();
    }

    private static List<Recipe> loadRecipes() {
        return recipeDatabaseUtil.extractRecipeList("");
    }

}
