package com.mobile.anvce.baking.application;

import android.app.Application;
import android.content.Context;

import com.mobile.anvce.baking.components.ApplicationComponent;
import com.mobile.anvce.baking.components.DaggerApplicationComponent;

public class RecipeApplication extends Application {

    public static RecipeApplication get(Context context) {
        return (RecipeApplication) context.getApplicationContext();
    }

    private ApplicationComponent applicationComponent;

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }
}
