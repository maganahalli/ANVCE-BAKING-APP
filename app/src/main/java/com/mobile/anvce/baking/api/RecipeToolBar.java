package com.mobile.anvce.baking.api;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.models.Recipe;

/**
 * Interface for Tool Bar display
 */
public interface RecipeToolBar {
    void populateToolbar(@NonNull AppCompatActivity activity, @NonNull DbRecipe recipe, @NonNull Toolbar toolbar,
                         @NonNull TextView recipeTitleView, @NonNull TextView yieldDescription, @NonNull ImageView mRecipeImageView,
                         int backgroundColor, int titleTextColor);


}

