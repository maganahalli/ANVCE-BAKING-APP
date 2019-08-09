package com.mobile.anvce.baking.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.activities.IngredientsListActivity;
import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.models.BakingAppConstants;

public class BaseRecipeWidget implements RecipeWidget, BakingAppConstants {


    @NonNull
    private static Intent buildRecipeDescriptionIntent(@NonNull Context context, @NonNull DbRecipe recipe) {
        Intent mainActivityIntent;
        mainActivityIntent = new Intent(context, IngredientsListActivity.class);
        mainActivityIntent.putExtra(RECIPE_ID, recipe.getId());
        mainActivityIntent.putExtra(SHOW_INGREDIENTS, true);
        return mainActivityIntent;
    }


    @NonNull
    public Intent buildWidgetClickIntent(@NonNull Context context, @NonNull DbRecipe recipe) {
        Intent widgetClickIntent;
        if (recipe == null) {
            widgetClickIntent = new Intent(context, MainBakingActivity.class);
        } else { // Set on click to open the corresponding detail activity
            final StepsPosition stepsAdapterPositionApi = new BaseStepsPosition(context);
            stepsAdapterPositionApi.resetStepsAdapterPosition();
            widgetClickIntent = buildRecipeDescriptionIntent(context, recipe);
        }
        return widgetClickIntent;
    }
}
