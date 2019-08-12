package com.mobile.anvce.baking.models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbRecipe;

import java.util.List;

public class BakingMainViewModel extends AndroidViewModel {
    private static final String TAG = BakingMainViewModel.class.getSimpleName();
    private final LiveData<List<DbRecipe>> dbRecipeList;

    public BakingMainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase recipeDatabase = AppDatabase.getInstance(this.getApplication());
        dbRecipeList = recipeDatabase.receipeDao().loadAllRecipes();
        Log.d(TAG, "Actively retrieving tha Recipe list from Database");
    }

    public LiveData<List<DbRecipe>> getDbRecipeList() {
        return dbRecipeList;
    }
}
