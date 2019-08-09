package com.mobile.anvce.baking.utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbIngredient;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.transformers.DbIngredientFromIngredient;
import com.mobile.anvce.baking.transformers.DbRecipeFromRecipe;
import com.mobile.anvce.baking.transformers.DbStepFromStep;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class RecipeDatabaseUtil {

    private static final String TAG = RecipeDatabaseUtil.class.getSimpleName();

    private AppDatabase recipeDataBase;
    private Context context;

    public RecipeDatabaseUtil(Context context, AppDatabase recipeDataBase) {
        this.recipeDataBase = recipeDataBase;
        this.context = context;
    }

    private void saveStepToDatabase(final Step step, final DbStep dbStep) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            DbStep retrievedRecord = recipeDataBase.receipeDao().retrieveStepById(step.getRecipeId(),dbStep.getStepId());
            if (retrievedRecord != null) {
                    Log.d(TAG, "Updating Step with id :" + step.getId() + "");
                    recipeDataBase.receipeDao().updateStep(dbStep);
                return;
            }
            Log.d(TAG, "Updating Step with id :" + dbStep.getRecipeId() + "");
            recipeDataBase.receipeDao().insertStep(dbStep);
        });


    }

    /**
     * Fetch data from a local resource.
     *
     * @return List<Recipe> list of recipes
     */
    public List<Recipe> extractRecipeList(String jsonString) {
        String fileContentString;
        fileContentString = "".equals(jsonString) ? buildJSONFromLocalResource(context) : jsonString;
        List<Recipe> recipes = new RecipeCustomDataConverter().toRecipeList(fileContentString);
        updateStepsWithRecipeId(recipes);
        storeIngredients(recipes);
        storeSteps(recipes);
        storeRecipes(recipes);
        //makeText(TAG, "------collectRecipesFromLocalResource--------", LENGTH_SHORT).show();
        return recipes;
    }

    private void updateStepsWithRecipeId(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            updateRecipeIdForSteps(recipe);
        }
    }

    private void storeRecipes(List<Recipe> recipeList) {
        for (final Recipe recipe : recipeList) {
            final DbRecipe dbRecipe = new DbRecipeFromRecipe().transform(recipe);
            saveRecipeToDatabase(recipe, dbRecipe);
        }
    }

    /**
     * Updates Steps with the corresponding Recipe Id
     *
     * @param recipe Recipe object
     */
    private void updateRecipeIdForSteps(@NonNull final Recipe recipe) {
        int stepCounter=0;
        for (Step step : recipe.getSteps()) {
            step.setRecipeId(recipe.getId());
            step.setStepId(stepCounter);
            stepCounter++;
        }
    }

    private void saveRecipeToDatabase(final Recipe recipe, final DbRecipe dbRecipe) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            DbRecipe retrievedRecord = recipeDataBase.receipeDao().retrieveRecipeById(recipe.getId());
            if (retrievedRecord != null) {
                if (retrievedRecord.getRecipeId() == recipe.getId()) {
                    Log.d(TAG, "Updating Recipe with id :" + recipe.getId() + "");
                    recipeDataBase.receipeDao().updateReceipe(dbRecipe);
                }
                return;
            }
            Log.d(TAG, "Updating Recipe with id :" + dbRecipe.getRecipeId() + "");
            recipeDataBase.receipeDao().insertRecipe(dbRecipe);
        });

    }


    /**
     * Fetch data from local resource (if network resource isn't available).
     *
     * @param context the application context
     * @return String  JSON string
     */
    public String buildJSONFromLocalResource(@NonNull Context context) {
        String json;
        try {
            InputStream inStream = context.getAssets().open("baking.json");
            int size = inStream.available();
            byte[] buffer = new byte[size];
            inStream.read(buffer);
            inStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private void storeIngredients(List<Recipe> recipeList) {
        for (Recipe recipe : recipeList) {
            storeIngredients(recipe);
        }

    }

    private void storeIngredients(Recipe recipe) {
        int ingredientId=0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            final DbIngredient dbIngredient = new DbIngredientFromIngredient().transform(ingredient);
            dbIngredient.setRecipeId(recipe.getId());
            dbIngredient.setIngredientId(ingredientId);
            storeIngredientToDatabase(ingredient, dbIngredient);
            ingredientId++;
        }

    }

    private void storeIngredientToDatabase(Ingredient ingredient, DbIngredient dbIngredient) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            DbIngredient retrievedRecord = recipeDataBase.receipeDao().retrieveIngredientById(dbIngredient.getRecipeId(),dbIngredient.getIngredientId());
            if (retrievedRecord != null) {
                Log.d(TAG, "Updating Ingredient with id :" + dbIngredient.getRecipeId() + "");
                recipeDataBase.receipeDao().updateIngredient(dbIngredient);
                return;
            }
            Log.d(TAG, "Inserting  Ingredient with id :" + dbIngredient.getRecipeId() + "");
            recipeDataBase.receipeDao().insertIngredient(dbIngredient);
        });

    }


    private void storeSteps(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            storeSteps(recipe);
        }

    }


    private void storeSteps(Recipe recipe) {
        for (Step step : recipe.getSteps()) {
            final DbStep dbStep = new DbStepFromStep().transform(step);
            saveStepToDatabase(step, dbStep);
        }
    }


}
