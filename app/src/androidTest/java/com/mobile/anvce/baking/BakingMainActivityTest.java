package com.mobile.anvce.baking;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.api.BaseUiDisplayFormat;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.utilities.RecipeDatabaseUtil;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


/**
 * This test demos a user clicking on a GridView item in Main Activity which opens up the
 * corresponding RecipeActivity.
 * This test does not utilize Idling Resources yet. If idling is set in the MenuActivity,
 * then this test will fail. See the IdlingResourcesTest for an identical test that
 * takes into account Idling Resources.
 */

public class BakingMainActivityTest {

    private static final int RECIPE_POSITION = 1;
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainBakingActivity> mActivityTestRule = new ActivityTestRule<>(MainBakingActivity.class);

    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */
    @Test
    public void clickGridViewItem_OpensItemListActivity() {
        final RecipeApplication application = (RecipeApplication) mActivityTestRule.getActivity().getApplication();
        final UiDisplayFormat beautifier = new BaseUiDisplayFormat((mActivityTestRule.getActivity()));
        final AppDatabase recipeDataBase = AppDatabase.getInstance(mActivityTestRule.getActivity());
        final RecipeDatabaseUtil recipeDatabaseUtil = new RecipeDatabaseUtil(mActivityTestRule.getActivity(), recipeDataBase);
        new TestUtils(recipeDataBase, recipeDatabaseUtil);
        Recipe recipe = TestUtils.getRecipeByPosition(RECIPE_POSITION);

        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // grid view item and clicks it.
        onView(withId(R.id.recipesListView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_POSITION, click()));


    }

}
