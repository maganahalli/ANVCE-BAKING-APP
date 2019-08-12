package com.mobile.anvce.baking.api;

import android.content.Context;
import android.content.Intent;

import com.mobile.anvce.baking.activities.IngredientsListActivity;
import com.mobile.anvce.baking.models.BakingAppConstants;

/**
 * Implementation of the IngredientsAndDescriptionIntent
 */
public class IngredientsAndDescriptionIntentBuilder implements IngredientsAndDescriptionIntent, BakingAppConstants {

    private final Context context;
    private final StepsPosition stepsPosition;

    public IngredientsAndDescriptionIntentBuilder(Context context, StepsPosition stepsPosition) {
        this.context = context;
        this.stepsPosition = stepsPosition;
    }

    @Override
    public Intent setUpIngredientsAndDescriptionIntent(int recipeId) {
        stepsPosition.resetStepsAdapterPosition();
        Intent intent = new Intent(context, IngredientsListActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        return intent;
    }
}
