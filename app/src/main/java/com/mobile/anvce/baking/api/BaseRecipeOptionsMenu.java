package com.mobile.anvce.baking.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.callback.StepNavigation;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.enums.SortOrder;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of the RecipeOptionsMenu API
 */
public class BaseRecipeOptionsMenu implements RecipeOptionsMenu, BakingAppConstants {

    private final IngredientsAndDescriptionIntent ingredientsAndDescriptionIntent;
    private final ResourceOverrides resourceOverrides;

    public BaseRecipeOptionsMenu(IngredientsAndDescriptionIntent ingredientsAndDescriptionIntent, ResourceOverrides resourceOverrides) {
        this.ingredientsAndDescriptionIntent = ingredientsAndDescriptionIntent;
        this.resourceOverrides = resourceOverrides;
    }

    @Override
    public BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemSelectedListener(@NonNull AppCompatActivity activity) {
        if (!(activity instanceof StepNavigation)) {
            return item -> false;
        }
        final StepNavigation callback = (StepNavigation) activity;
        return item -> {
            switch (item.getItemId()) {
                case R.id.navigation_prev:
                    callback.loadNewStep(SortOrder.DESCENDING);
                    return true;
                case R.id.navigation_next:
                    callback.loadNewStep(SortOrder.ASCENDING);
                    return true;
                case R.id.navigation_home:
                    Intent intent = new Intent(activity, MainBakingActivity.class);
                    activity.startActivity(intent);
                    return true;
            }
            return false;
        };
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull AppCompatActivity activity, @NonNull MenuItem item) {
        activity.finish();
        Intent intent = ingredientsAndDescriptionIntent.setUpIngredientsAndDescriptionIntent(item.getItemId());
        activity.startActivity(intent);
        return false;

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull AppCompatActivity activity, @NonNull Menu menu) {

        List<Recipe> recipes = restoreSharedPreference(activity);
        menu.clear();
        for (Recipe recipe : recipes) {
            final int iconResource = resourceOverrides.getRecipeIconOverrideMap().get(recipe.getName());
            menu.add(0, recipe.getId(), Menu.NONE, recipe.getName()).setIcon(iconResource);
        }
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    // Restore preferences
    private List<Recipe> restoreSharedPreference(@NonNull AppCompatActivity activity) {
        SharedPreferences mPreferences = activity.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String recipeListAsString = mPreferences.getString(RECIPE_LIST, "");
        if (TextUtils.isEmpty(recipeListAsString)) {
            return new ArrayList<>();
        }

        return new RecipeCustomDataConverter().toRecipeList(recipeListAsString);

    }

}
