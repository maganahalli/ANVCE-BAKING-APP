package com.mobile.anvce.baking.components;

import android.app.Application;
import android.content.Context;

import com.mobile.anvce.baking.activities.IngredientsListActivity;
import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.activities.RecipeStepFragment;
import com.mobile.anvce.baking.activities.StepDetailActivity;
import com.mobile.anvce.baking.adapters.IngredientsAdapter;
import com.mobile.anvce.baking.adapters.RecipeAdapter;
import com.mobile.anvce.baking.api.IngredientsAndDescriptionIntent;
import com.mobile.anvce.baking.api.RecipeOptionsMenu;
import com.mobile.anvce.baking.api.RecipeToolBar;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.api.ResourceOverrides;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.application.ApplicationModule;
import com.mobile.anvce.baking.application.RecipeApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides application resources module
 *
 * @author Venky maganhalli.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application getApplication();

    Context getContext();

    UiDisplayFormat getFormatterApi();

    RecipeOptionsMenu getRecipeOptionsMenu();

    RecipesFacade getRecipesFacade();

    ResourceOverrides getResourceOverrides();

    StepsPosition getStepsAdapterPosition();

    RecipeToolBar getRecipeToolbar();


    void inject(RecipeApplication recipeApplication);

    void inject(MainBakingActivity mainActivity);

    void inject(RecipeAdapter recipeAdapter);

    void inject(IngredientsAdapter ingredientsAdapter);

    void inject(IngredientsListActivity ingredientsListActivity);

    void inject(RecipeStepFragment stepsFragment);

    void inject(StepDetailActivity stepDetailActivity);

    IngredientsAndDescriptionIntent provideIngredientsAndDescriptionIntentBuilder();

}
