package com.mobile.anvce.baking.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.adapters.IngredientsAdapter;
import com.mobile.anvce.baking.adapters.RecipeStepsAdapter;
import com.mobile.anvce.baking.api.RecipeStepsOnClickHandler;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.callback.TwoPane;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.transformers.DbRecipeFromRecipe;
import com.mobile.anvce.baking.viewholders.StepViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Manages Recipe steps .
 */
public class RecipeStepFragment extends Fragment implements BakingAppConstants {

    @BindView(R.id.ingredientsLayout)
    View ingredientsLayout;
    @Inject
    RecipesFacade recipesFacade;
    @BindView(R.id.directionsRecyclerView)
    RecyclerView recyclerView;
    RecipeStepsAdapter adapter;
    @BindDrawable(R.drawable.touch_selector)
    Drawable touchSelector;
    @BindDrawable(R.drawable.touch_selector_selected)
    Drawable touchSelectorSelected;
    private boolean isShowIngredients = false;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable mListState;
    private ArrayList<DbStep> mSteps;
    private int recipeId;
    @Inject
    StepsPosition stepsPosition;
    @BindDrawable(R.drawable.ic_arrow_drop_down)
    Drawable arrowDown;
    @BindView(R.id.arrowImage)
    ImageView arrowImage;
    @BindDrawable(R.drawable.ic_arrow_drop_up)
    Drawable arrowUp;
    @BindColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;
    @BindView(R.id.ingredient_list)
    RecyclerView ingredientListView;
    Unbinder unbinder;
    private Recipe mRecipe;


    public RecipeStepFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        final RecipeApplication application = (RecipeApplication) getActivity().getApplication();
        application.getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mSteps = stepsPosition.getArrayOfSteps(arguments);
        recipeId = arguments.getInt(RECIPE_ID);
        isShowIngredients = arguments.getBoolean(SHOW_INGREDIENTS, false);
        mRecipe = arguments.getParcelable(RECIPE);


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        unbinder = ButterKnife.bind(this, view);
        arrowImage.setColorFilter(colorPrimaryDark);
        ingredientListView.setVisibility(isShowIngredients ? View.VISIBLE : View.GONE);
        arrowImage.setImageDrawable(isShowIngredients ? arrowUp : arrowDown);
        final DbRecipe recipe = new DbRecipeFromRecipe().transform(mRecipe);
        populateIngredientList(recipe);
        setupRecyclerView(mSteps, recipeId);
        return view;
    }

    private void populateIngredientList(@NonNull final DbRecipe recipe) {
        assert ingredientListView != null;
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Ingredient> ingredients = recipesFacade.getIngredients(recipe);
            ingredientListView.setAdapter(new IngredientsAdapter(getActivity(), ingredients));
        });

    }

    private void setupRecyclerView(final ArrayList<DbStep> steps, final int recipeId) {
        // set a LinearLayoutManager with default vertical orientation
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutFrozen(false);
        adapter = new RecipeStepsAdapter(getContext(), mSteps, new RecipeStepsOnClickHandler() {
            @Override
            public void onClick(StepViewHolder viewHolder, DbStep step) {
                if (((TwoPane) getActivity()).isTwoPane()) {
                    Bundle args = new Bundle();
                    args.putParcelable(STEP_EXTRA, step);
                    args.putInt(RECIPE_ID, recipeId);
                    args.putParcelableArrayList(ARG_STEPS_ARRAY, steps);
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.steps_detail_container, stepDetailFragment, STEP_DETAIL_FRAGMENT_TAG)
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                    intent.putExtra(STEP_EXTRA, step);
                    intent.putExtra(RECIPE_ID, recipeId);
                    intent.putExtra(ARG_STEPS_ARRAY, steps);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        smoothScrollToTarget();
        if (mListState != null) {
            linearLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        mListState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Retrieve list state and list/item positions
        if (savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }


    protected void smoothScrollToTarget() {
        final int target;
        try {
            target = stepsPosition.getStepsPosition();
        } catch (NumberFormatException e) {
            return;
        }
        if ((target < 0) || (target > adapter.getItemCount() - 1)) {
            return;
        }
        linearLayoutManager.smoothScrollToPosition(recyclerView, null, target);
    }

    @OnClick(R.id.ingredientsLayout)
    public void toggleIngredients() {
        if (ingredientListView.getVisibility() == View.VISIBLE) {
            arrowImage.setImageDrawable(arrowDown);
            ingredientListView.setVisibility(View.GONE);
        } else {
            arrowImage.setImageDrawable(arrowUp);
            ingredientListView.setVisibility(View.VISIBLE);
        }
    }

    public void highlightStep(@NonNull DbStep step) {
        for (int i = 0; i < mSteps.size(); i++) {
            DbStep thisStep = mSteps.get(i);
            if (thisStep.getId() == step.getStepId()) {
                RecipeStepsAdapter adapter
                        = (RecipeStepsAdapter) recyclerView.getAdapter();
                stepsPosition.setStepsPosition(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }

    }

}
