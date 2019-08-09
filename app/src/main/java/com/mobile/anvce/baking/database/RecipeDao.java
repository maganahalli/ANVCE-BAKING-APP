package com.mobile.anvce.baking.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecipe(DbRecipe recipe);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStep(DbStep step);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIngredient(DbIngredient ingredient);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateReceipe(DbRecipe receipe);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateStep(DbStep step);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateIngredient(DbIngredient ingredient);

    @Query("SELECT * FROM RECIPE WHERE recipeId=:id")
    DbRecipe retrieveRecipeById(int id);

    @Query("SELECT * FROM RECIPE")
    List<DbRecipe> fetchAllRecipes();


    @Query("SELECT * FROM STEP WHERE recipeId=:recipeId AND stepId=:stepId")
    DbStep retrieveStepById(int recipeId, int stepId);

    @Query("SELECT * FROM INGREDIENT WHERE recipeId=:recipeId AND ingredientId=:ingredientId")
    DbIngredient retrieveIngredientById(int recipeId, int ingredientId);

    @Query("SELECT * FROM STEP WHERE recipeId=:recipeId ")
    List<DbStep> retrieveStepByRecipeId(int recipeId);

    @Query("SELECT * FROM INGREDIENT WHERE recipeId=:recipeId ")
    List<DbIngredient> retrieveIngredientByRecipeId(int recipeId);


}
