package com.mobile.anvce.baking.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.TaskStackBuilder;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.BaseRecipeWidget;
import com.mobile.anvce.baking.api.BaseRecipesFacade;
import com.mobile.anvce.baking.api.BaseResourceOverrides;
import com.mobile.anvce.baking.api.BaseUiDisplayFormat;
import com.mobile.anvce.baking.api.RecipeWidget;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.api.ResourceOverrides;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Ingredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Venkatesh Maganahalli
 */

public class RecipeWidgetProvider extends AppWidgetProvider implements BakingAppConstants {

    static final String TAG = RecipeWidgetProvider.class.getSimpleName();
    static final RecipeWidget ingredientsWidgetApi = new BaseRecipeWidget();
    static final ResourceOverrides resourceOverridesApi = new BaseResourceOverrides();

    public static void handleActionViewIngredients(@NonNull final Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_name);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.imageView);
        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        final RecipesFacade recipesFacade = new BaseRecipesFacade(context);
        List<DbRecipe> recipes = new ArrayList<>();
        recipes.addAll(recipesFacade.fetchAllRecipes());
        DbRecipe recipe = recipesFacade.anyRecipe(recipes);
        editor.putInt(PREFS_WIDGET_RECIPE_ID, recipe.getId());
        final Set<String> ingredientItems = new HashSet<>();
        final UiDisplayFormat formatterApi = new BaseUiDisplayFormat(context);
        List<Ingredient> ingredientList = new RecipeCustomDataConverter().toIngredientList(recipe.getIngredientsListAsString());
        for (Ingredient ingredient : ingredientList) {
            ingredientItems.add(formatterApi.formatIngredientForDisplay(ingredient));
        }
        editor.putStringSet(PREFS_WIDGET_RECIPE_INGREDIENTS, ingredientItems);
        editor.apply();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_recipes_app_widget);
        final Intent widgetClickIntent = ingredientsWidgetApi.buildWidgetClickIntent(context, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Widgets allow click handlers to only launch pending intents
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
        remoteViews.setTextViewText(R.id.recipe_name, recipe.getName());

        final int iconResource = recipe.getIconResource() == 0
                ? resourceOverridesApi.getRecipeIconOverrideMap().get(recipe.getName()) : recipe.getIconResource();
        remoteViews.setImageViewResource(R.id.imageView, iconResource);

        Intent ingredientsIntent = new Intent(context, RecipeWidgetRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widgetListView, ingredientsIntent);

        // template to handle the click listener for each item
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(widgetClickIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled():last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, RecipeWidgetBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecipeWidgetBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 3 seconds
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                60000, pi);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            handleActionViewIngredients(context);
        }
        super.onReceive(context, intent);
    }

}
