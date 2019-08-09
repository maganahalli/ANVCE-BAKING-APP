package com.mobile.anvce.baking.callback;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.enums.SortOrder;
import com.mobile.anvce.baking.models.Step;

import java.util.List;

/**
 * callback that manages step navigation
 */
public interface StepNavigation {
    BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemSelectedListener();

    void loadNewStep(@NonNull final SortOrder sort);

    void loadNewStep(@NonNull DbStep step, int recipeId, @NonNull List<DbStep> steps);

    void manageBottomNavigationItems();

    void updateSelectedStep(@NonNull DbStep step, int position);

}
