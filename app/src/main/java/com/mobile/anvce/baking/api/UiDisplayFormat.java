package com.mobile.anvce.baking.api;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.database.DbIngredient;
import com.mobile.anvce.baking.models.Ingredient;

/**
 * API that provides various UI display formatting functionality
 *
 * @author Venky Maganahalli
 */
public interface UiDisplayFormat {

    String formatIngredientForDisplay(@NonNull final Ingredient ingredient);

    String formatIngredientForDisplay(@NonNull final DbIngredient dbIngredient);


    String formatServings(int servings);

    String formatServings(@NonNull String servingsFormat, int servings);
}