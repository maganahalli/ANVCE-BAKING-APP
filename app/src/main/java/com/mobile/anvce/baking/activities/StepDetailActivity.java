package com.mobile.anvce.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.RecipeOptionsMenu;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.callback.StepNavigation;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.enums.SortOrder;
import com.mobile.anvce.baking.models.BakingAppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity displaying the step detail
 */
public class StepDetailActivity extends CommonActivity implements StepNavigation, BakingAppConstants {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @Inject
    RecipeOptionsMenu recipeOptionsMenuPresenter;
    @Inject
    StepsPosition stepsPosition;
    private int recipeId;
    private DbStep step = new DbStep();
    private List<DbStep> steps = new ArrayList<>();

    @Override
    public OnNavigationItemSelectedListener createOnNavigationItemSelectedListener() {
        return recipeOptionsMenuPresenter.createOnNavigationItemSelectedListener(this);
    }


    @Override
    public void loadNewStep(@NonNull SortOrder sort) {
        if (sort.isAscending()) {
            int position = 0;
            for (DbStep stepToNavigate : steps) {
                if (stepToNavigate.getStepId() > step.getStepId()) {
                    Log.d(TAG, String.format("new step: %s", stepToNavigate));
                    updateSelectedStep(stepToNavigate, position);
                    loadNewStep(stepToNavigate, recipeId, steps);
                    break;
                }
                position++;
            }
        } else {
            for (int counter = steps.size() - 1; counter >= 0; counter--) {
                DbStep stepToNavigate = steps.get(counter);
                if (stepToNavigate.getStepId() < step.getStepId()) {
                    Log.d(TAG, String.format("new step: %s", stepToNavigate));
                    updateSelectedStep(stepToNavigate, counter);
                    loadNewStep(stepToNavigate, recipeId, steps);
                    break;
                }
            }
        }
    }

    @Override
    public void loadNewStep(@NonNull DbStep step, int recipeId, @NonNull List<DbStep> steps) {
        Bundle args = new Bundle();
        args.putParcelable(STEP_EXTRA, step);
        final ArrayList<DbStep> stepsArray = getDbStepArray(steps);
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(STEP_EXTRA, step);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(ARG_STEPS_ARRAY, stepsArray);
        startActivity(intent);
    }

    private ArrayList<DbStep> getDbStepArray(List<DbStep> steps) {
        ArrayList<DbStep> arrayList = new ArrayList<>();
        for (DbStep step : steps) {
            arrayList.add(step);
        }
        return arrayList;
    }

    @Override
    public void manageBottomNavigationItems() {
        final Menu menu = navigationView.getMenu();
        if (stepsPosition.getStepsPosition() == 0) {
            menu.removeItem(R.id.navigation_prev);
        } else if (stepsPosition.getStepsPosition() == steps.size() - 1) {
            menu.removeItem(R.id.navigation_next);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, IngredientsListActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecipeApplication application = (RecipeApplication) this.getApplication();
        application.getApplicationComponent().inject(this);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        step = getIntent().getParcelableExtra(STEP_EXTRA);
        recipeId = getIntent().getIntExtra(RECIPE_ID, 1);
        final ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(ARG_STEPS_ARRAY);
        steps.clear();
        for (Parcelable item : items) {
            if (item instanceof DbStep) {
                steps.add((DbStep) item);
            }
        }
        navigationView.setOnNavigationItemSelectedListener(createOnNavigationItemSelectedListener());
        populateToolbar(recipeId);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(STEP_EXTRA, step);
            arguments.putInt(RECIPE_ID, recipeId);
            arguments.putParcelableArrayList(ARG_STEPS_ARRAY, getStepsArrayList(steps));
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.steps_detail_container, stepDetailFragment).commit();
        }
    }

    private ArrayList<DbStep> getStepsArrayList(final List<DbStep> steps) {
        ArrayList<DbStep> items = new ArrayList<>();
        for (DbStep item : items) {
            items.add(item);
        }
        return items;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        recipeOptionsMenuPresenter.onOptionsItemSelected(this, item);
        return false;
    }

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        recipeOptionsMenuPresenter.onPrepareOptionsMenu(this, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateSelectedStep(@NonNull DbStep step, int position) {
        stepsPosition.setStepsPosition(position);
    }
}

