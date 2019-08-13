package com.mobile.anvce.baking;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.mobile.anvce.baking.activities.IngredientsListActivity;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.utilities.RecipeDatabaseUtil;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class IngredientsListActivityTest implements BakingAppConstants {
    private static final int RECIPE_POSITION = 1;
    private static final int STEP_POSITION = 3;
    private Recipe recipe = new Recipe();
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<IngredientsListActivity> mActivityRule =
            new ActivityTestRule<IngredientsListActivity>(IngredientsListActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    final AppDatabase recipeDataBase = AppDatabase.getInstance(targetContext);
                    final RecipeDatabaseUtil recipeDatabaseUtil = new RecipeDatabaseUtil(targetContext, recipeDataBase);

                    new TestUtils(recipeDataBase, recipeDatabaseUtil);
                    recipe = TestUtils.getRecipeByPosition(RECIPE_POSITION);
                    recipe.setId(1);
                    Intent result = new Intent(targetContext, IngredientsListActivity.class);
                    result.putExtra(RECIPE_ID, recipe.getId());
                    result.putExtra(BakingAppConstants.RECIPE, recipe);
                    return result;
                }
            };

    // Convenience helper
    private static RecyclerViewMatcher withRecyclerView() {
        return new RecyclerViewMatcher(R.id.directionsRecyclerView);
    }

    /**
     * Check that the indexed step contains the correct short description
     */
    @Test
    public void step_descendant_contains_shortDescription() {
        final List<Step> steps = recipe.getSteps();
        if (steps.isEmpty()) return;
        final Step step = steps.get(STEP_POSITION);
        onView(withRecyclerView().atPosition(STEP_POSITION))
                .check(matches(hasDescendant(withText(step.getShortDescription()))));
    }

}
