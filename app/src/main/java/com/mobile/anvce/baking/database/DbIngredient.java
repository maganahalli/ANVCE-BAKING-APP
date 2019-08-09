package com.mobile.anvce.baking.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Models Ingredient for Retrofit2 library
 *
 * @author Venkatesh Maganahalli
 */
@Entity(tableName = "INGREDIENT")
public class DbIngredient {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double quantity;
    private String measure = "";
    private String ingredient = "";
    private int recipeId;
    private int ingredientId;

    public DbIngredient(Double quantity, String measure, String ingredient, int recipeId, int ingredientId) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

    @Ignore
    public DbIngredient() {
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


}
