package com.mobile.anvce.baking.api;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.callback.StepNavigation;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.enums.SortOrder;
import com.mobile.anvce.baking.executors.AppExecutors;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the RecipeOptionsMenu API
 */
public class BaseRecipeOptionsMenu implements RecipeOptionsMenu {

    private final IngredientsAndDescriptionIntent ingredientsAndDescriptionIntent;
    private final ResourceOverrides resourceOverrides;

    public BaseRecipeOptionsMenu(IngredientsAndDescriptionIntent ingredientsAndDescriptionIntent, ResourceOverrides resourceOverrides) {
        this.ingredientsAndDescriptionIntent = ingredientsAndDescriptionIntent;
        this.resourceOverrides = resourceOverrides;
    }

    @Override
    public BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemSelectedListener(@NonNull AppCompatActivity activity) {
        if (!(activity instanceof StepNavigation)) {
            return new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return false;
                }
            };
        }
        final StepNavigation callback = (StepNavigation) activity;
        return new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }

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

        final RecipesFacade recipesFacade = new BaseRecipesFacade(activity);
        List<DbRecipe> recipes = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<DbRecipe> recipeList = recipesFacade.fetchAllRecipes();

            if (recipeList != null) {
                menu.clear();
                for (DbRecipe recipe : recipes) {
                    final int iconResource = recipe.getIconResource() == 0
                            ? resourceOverrides.getRecipeIconOverrideMap().get(recipe.getName()) : recipe.getIconResource();
                    menu.add(0, recipe.getId(), Menu.NONE, recipe.getName()).setIcon(iconResource);
                }
                ActionBar actionBar = activity.getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

        });
    }
}
