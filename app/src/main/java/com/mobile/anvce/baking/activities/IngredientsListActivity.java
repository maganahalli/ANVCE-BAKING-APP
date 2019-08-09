package com.mobile.anvce.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.RecipeOptionsMenu;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.callback.StepNavigation;
import com.mobile.anvce.baking.callback.TwoPane;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.enums.SortOrder;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.transformers.RecipeFromDbRecipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsListActivity extends CommonActivity implements TwoPane, StepNavigation, BakingAppConstants {

    final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @Inject
    RecipeOptionsMenu recipeOptionsMenu;
    @Inject
    StepsPosition stepsPosition;
    @BindView(R.id.steps_container)
    View stepsContainer;
    @BindView(R.id.steps_detail_container)
    @Nullable
    View stepsDetailContainer;
    private boolean isShowIngredients = false;
    private int mRecipeId;
    private boolean mTwoPane;
    private DbRecipe recipe = new DbRecipe();
    private AppDatabase recipeDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecipeApplication application = (RecipeApplication) this.getApplication();
        application.getApplicationComponent().inject(this);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mRecipeId = savedInstanceState.getInt(RECIPE_ID, 1);
            isShowIngredients = savedInstanceState.getBoolean(SHOW_INGREDIENTS, false);
            Log.d(TAG, String.format("savedInstanceState != null, mRecipeId: %s isShowIngredients: %s", mRecipeId, isShowIngredients));
        } else {
            Intent intent = getIntent();
            mRecipeId = intent.getIntExtra(RECIPE_ID, 1);
            isShowIngredients = intent.getBooleanExtra(SHOW_INGREDIENTS, false);
            Log.d(TAG, String.format("savedInstanceState == null, mRecipeId: %s isShowIngredients: %s", mRecipeId, isShowIngredients));
        }

        recipeDataBase = AppDatabase.getInstance(this);
        AppExecutors.getInstance().diskIO().execute(() -> {
            final DbRecipe retrievedRecipe = recipeDataBase.receipeDao().retrieveRecipeById(mRecipeId);
            assert retrievedRecipe != null;
            Log.d(TAG, "Recipe Name:" + retrievedRecipe.getName() + "");

            runOnUiThread(() -> {
                if (retrievedRecipe != null) {
                    recipe = retrievedRecipe;
                    setContentView(R.layout.activity_ingredients_list);

                    if (null == recipe) {
                        return;
                    }
                    ButterKnife.bind(this);
                    Log.d(TAG, "Binding done for " + retrievedRecipe.getName() + "");

                    supportStartPostponedEnterTransition();
                    if (navigationView == null) {
                        navigationView = new BottomNavigationView(this);
                    }
                    navigationView.setOnNavigationItemSelectedListener(createOnNavigationItemSelectedListener());
                    navigationView.setVisibility(getResources().getBoolean(R.bool.navBarVisible) ? View.VISIBLE : View.GONE);
                    determineNavigation(savedInstanceState);

                }
            });
        });


    }

    private void determineNavigation(Bundle savedInstanceState) {
        if (stepsDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        populateToolbar(recipe);
        final Recipe currentRecipe = new RecipeFromDbRecipe().transform(recipe);
        if (stepsContainer != null) {
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_STEPS_ARRAY, currentRecipe.getSteps());
            args.putInt(RECIPE_ID, mRecipeId);
            args.putBoolean(SHOW_INGREDIENTS, isShowIngredients);
            args.putParcelable(RECIPE, currentRecipe);
            RecipeStepFragment stepsFragment = new RecipeStepFragment();
            stepsFragment.setArguments(args);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.steps_container, stepsFragment, STEPS_FRAGMENT_TAG)
                        .commit();
            }
        }
        if (mTwoPane) {
            Bundle args = new Bundle();
            final int stepsAdapterPosition = stepsPosition.getStepsPosition();
            args.putParcelable(STEP_EXTRA, recipe.getSteps().get(stepsAdapterPosition));
            args.putParcelableArrayList(ARG_STEPS_ARRAY, currentRecipe.getSteps());
            args.putInt(RECIPE_ID, mRecipeId);
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_detail_container, stepDetailFragment, STEP_DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemSelectedListener() {
        return recipeOptionsMenu.createOnNavigationItemSelectedListener(this);

    }

    public RecipeStepFragment getRecipeStepsFragment() {
        return (RecipeStepFragment) getSupportFragmentManager().findFragmentByTag(STEPS_FRAGMENT_TAG);
    }

    @Override
    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    public void loadNewStep(@NonNull SortOrder sort) {
        final int position = stepsPosition.getStepsPosition();

        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<DbStep> steps = recipeDataBase.receipeDao().retrieveStepByRecipeId(mRecipeId);
            assert steps != null;
            Log.d(TAG, " Number of steps :" + steps.size() + "");

            runOnUiThread(() -> {
                if (!steps.isEmpty()) {

                    final DbStep step = steps.get(position);
                    if (sort.isAscending()) {
                        for (DbStep stepToNavigate : steps) {
                            if (stepToNavigate.getStepId() > step.getStepId()) {
                                Log.d(TAG, String.format("new step: %s", stepToNavigate));
                                updateSelectedStep(stepToNavigate, position);
                                loadNewStep(stepToNavigate, recipe.getId(), steps);
                                break;
                            }
                        }
                    } else {
                        for (int index = steps.size() - 1; index >= 0; index--) {
                            DbStep stepToNavigate = steps.get(index);
                            if (stepToNavigate.getStepId() < step.getStepId()) {
                                Log.d(TAG, String.format("new step: %s", stepToNavigate));
                                updateSelectedStep(stepToNavigate, index);
                                loadNewStep(stepToNavigate, recipe.getId(), steps);
                                break;
                            }
                        }
                    }


                }
            });
        });


    }

    @Override
    public void loadNewStep(@NonNull DbStep step, int recipeId, @NonNull List<DbStep> steps) {

        Bundle args = new Bundle();
        args.putParcelable(STEP_EXTRA, step);
        final ArrayList<DbStep> stepsArray = getArraySteps(steps);
        args.putParcelableArrayList(ARG_STEPS_ARRAY, stepsArray);
        args.putInt(RECIPE_ID, recipeId);
        if (mTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.steps_detail_container, stepDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            manageBottomNavigationItems();
            return;
        }
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(STEP_EXTRA, step);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(ARG_STEPS_ARRAY, stepsArray);
        startActivity(intent);
    }

    private ArrayList<DbStep> getArraySteps(List<DbStep> steps) {
        ArrayList<DbStep> arraySteps = new ArrayList<>();
        for (DbStep step : steps) {
            arraySteps.add(step);
        }
        return arraySteps;
    }

    @Override
    public void manageBottomNavigationItems() {
        final Menu menu = navigationView.getMenu();
        if (stepsPosition.getStepsPosition() == 0) {
            menu.removeItem(R.id.navigation_prev);
        } else if (stepsPosition.getStepsPosition() == recipe.getSteps().size() - 1) {
            menu.removeItem(R.id.navigation_next);
        }

    }

    @Override
    public void updateSelectedStep(@NonNull DbStep step, int position) {
        stepsPosition.setStepsPosition(position);
        getStepsFragment().highlightStep(step);
    }

    public RecipeStepFragment getStepsFragment() {
        return (RecipeStepFragment) getSupportFragmentManager().findFragmentByTag(STEPS_FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainBakingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, mRecipeId);
        final int stepsAdapterPosition = stepsPosition.getStepsPosition();
        outState.putInt(STEP_ID, stepsAdapterPosition);
        outState.putBoolean(SHOW_INGREDIENTS, isShowIngredients);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        recipeOptionsMenu.onOptionsItemSelected(this, item);
        return false;
    }

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        recipeOptionsMenu.onPrepareOptionsMenu(this, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}
