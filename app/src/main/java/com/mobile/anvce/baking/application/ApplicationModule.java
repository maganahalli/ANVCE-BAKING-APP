package com.mobile.anvce.baking.application;

import android.app.Application;
import android.content.Context;

import com.mobile.anvce.baking.api.BaseRecipeOptionsMenu;
import com.mobile.anvce.baking.api.BaseRecipeToolBar;
import com.mobile.anvce.baking.api.BaseRecipesFacade;
import com.mobile.anvce.baking.api.BaseResourceOverrides;
import com.mobile.anvce.baking.api.BaseStepsPosition;
import com.mobile.anvce.baking.api.BaseUiDisplayFormat;
import com.mobile.anvce.baking.api.IngredientsAndDescriptionIntent;
import com.mobile.anvce.baking.api.IngredientsAndDescriptionIntentBuilder;
import com.mobile.anvce.baking.api.RecipeOptionsMenu;
import com.mobile.anvce.baking.api.RecipeToolBar;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.api.ResourceOverrides;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.api.UiDisplayFormat;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    UiDisplayFormat provideFormatter(Context context) {
        return new BaseUiDisplayFormat(context);
    }

    @Provides
    @Singleton
    IngredientsAndDescriptionIntent provideIngredientsAndDescriptionIntent(Context context, StepsPosition stepsPosition) {
        return new IngredientsAndDescriptionIntentBuilder(context, stepsPosition);
    }

    @Provides
    @Singleton
    RecipeOptionsMenu provideRecipeOptionsMenu(IngredientsAndDescriptionIntent ingredientsAndDescriptionIntent,
                                               ResourceOverrides resourceOverrides) {
        return new BaseRecipeOptionsMenu(ingredientsAndDescriptionIntent, resourceOverrides);
    }

    @Provides
    @Singleton
    RecipesFacade provideRecipesFacade(Context context) {
        return new BaseRecipesFacade(context);
    }

    @Provides
    @Singleton
    ResourceOverrides provideResourceOverrides() {
        return new BaseResourceOverrides();
    }

    @Provides
    @Singleton
    StepsPosition provideStepsPosition(Context context) {
        return new BaseStepsPosition(context);
    }

    @Provides
    @Singleton
    RecipeToolBar provideToolbar(UiDisplayFormat formatter, ResourceOverrides resourceOverrides) {
        return new BaseRecipeToolBar(formatter, resourceOverrides);
    }

}
