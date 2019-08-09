package com.mobile.anvce.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.BaseRecipesFacade;
import com.mobile.anvce.baking.api.BaseUiDisplayFormat;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkatesh maganahalli
 */

public class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, BakingAppConstants {

    private final UiDisplayFormat beautifier;
    private final Context mContext;
    private final RecipesFacade recipesFacade;
    private List<String> ingredientDescriptions = new ArrayList<>();


    public RecipeWidgetRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.beautifier = new BaseUiDisplayFormat(mContext);
        this.recipesFacade = new BaseRecipesFacade(mContext);
    }

    @Override
    public int getCount() {
        return ingredientDescriptions == null ? 0 : ingredientDescriptions.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                ingredientDescriptions == null || position >= ingredientDescriptions.size()) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_row);
        remoteViews.setTextViewText(R.id.ingredientItem, ingredientDescriptions.get(position));
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        final int recipeId = prefs.getInt(PREFS_WIDGET_RECIPE_ID, 0);
        DbRecipe recipe = recipesFacade.loadRecipe(recipeId);
        ingredientDescriptions.clear();
        List<Ingredient> ingredientList = new RecipeCustomDataConverter().toIngredientList(recipe.getIngredientsListAsString());
        for (Ingredient ingredient : ingredientList) {
            ingredientDescriptions.add(beautifier.formatIngredientForDisplay(ingredient));
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (ingredientDescriptions != null) {
            ingredientDescriptions = null;
        }
    }
}
