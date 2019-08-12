package com.mobile.anvce.baking.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.models.Recipe;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeCustomDataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromIngredientList(List<DbIngredient> ingredientList) {
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DbIngredient>>() {
        }.getType();
        return gson.toJson(ingredientList, type);
    }

    @TypeConverter // note this annotation
    public String fromStepsList(List<DbStep> stepsList) {
        if (stepsList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DbStep>>() {
        }.getType();
        return gson.toJson(stepsList, type);
    }

    @TypeConverter // note this annotation
    public String fromRecipeList(List<Recipe> recipeList) {
        if (recipeList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe>>() {
        }.getType();
        return gson.toJson(recipeList, type);
    }

    @TypeConverter // note this annotation
    public List<Ingredient> toIngredientList(String ingredientString) {
        if (ingredientString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        return gson.fromJson(ingredientString, type);
    }

    @TypeConverter // note this annotation
    public ArrayList<com.mobile.anvce.baking.models.Step> toStepsList(String stepsString) {
        if (stepsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<com.mobile.anvce.baking.models.Step>>() {
        }.getType();
        return gson.fromJson(stepsString, type);
    }

    @TypeConverter // note this annotation
    public List<Recipe> toRecipeList(String recipeListAsString) {
        if (recipeListAsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe>>() {
        }.getType();
        return gson.fromJson(recipeListAsString, type);
    }


}
