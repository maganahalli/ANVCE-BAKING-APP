package com.mobile.anvce.baking.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.database.DbRecipe;

public interface RecipeWidget {

    Intent buildWidgetClickIntent(@NonNull Context context, @NonNull DbRecipe recipe);

}
