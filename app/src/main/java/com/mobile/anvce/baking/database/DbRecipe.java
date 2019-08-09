package com.mobile.anvce.baking.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.models.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Models Recipe for Retrofit2 library
 *
 * @author Venkatesh Maganahalli
 */
@Entity(tableName = "RECIPE")
public class DbRecipe {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    private String name = "";
    private String ingredientsListAsString;
    private String stepsListAsString;
    private Integer servings;
    private String image = "";
    private Integer iconResource = R.drawable.ic_yellow_cake;

    public DbRecipe(int recipeId, String name, String ingredientsListAsString, String stepsListAsString, Integer servings, String image) {
        this.recipeId = id;
        this.name = name;
        this.ingredientsListAsString = ingredientsListAsString;
        this.stepsListAsString = stepsListAsString;
        this.servings = servings;
        this.image = image;
    }

    @Ignore
    public DbRecipe() {
    }

    public Integer getIconResource() {
        return iconResource;
    }

    public void setIconResource(Integer iconResource) {
        this.iconResource = iconResource;
    }

    public String getStepsListAsString() {
        return stepsListAsString;
    }

    public void setStepsListAsString(String stepsListAsString) {
        this.stepsListAsString = stepsListAsString;
    }

    public String getIngredientsListAsString() {
        return ingredientsListAsString;
    }

    public void setIngredientsListAsString(String ingredientsListAsString) {
        this.ingredientsListAsString = ingredientsListAsString;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return new RecipeCustomDataConverter().toIngredientList(ingredientsListAsString);
    }

    public ArrayList<Step> getSteps() {
        return new RecipeCustomDataConverter().toStepsList(stepsListAsString);
    }

}
