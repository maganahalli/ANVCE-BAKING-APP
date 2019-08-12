package com.mobile.anvce.baking.activities;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.RecipeToolBar;
import com.mobile.anvce.baking.api.RecipesFacade;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.executors.AppExecutors;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.transformers.RecipeFromDbRecipe;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;

public abstract class CommonActivity extends AppCompatActivity {
   private  final String TAG = this.getClass().getSimpleName();

    @BindColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;
    @Nullable
    @BindView(R.id.recipeImage)
    ImageView mRecipeImageView;
    @Nullable
    @BindView(R.id.recipe_name)
    TextView recipeTitleView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindColor(R.color.white)
    int white;
    @Nullable
    @BindView(R.id.yieldDescription)
    TextView yieldDescription;

    @Inject
    RecipesFacade recipesFacade;
    @Inject
    RecipeToolBar toolbarPresenter;

    public void populateToolbar(int mRecipeId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            final DbRecipe retrievedRecipe = AppDatabase.getInstance(this).receipeDao().retrieveRecipeById(mRecipeId);
            runOnUiThread(() -> {
                if(retrievedRecipe != null) {
                    Log.d(TAG, "Recipe Name:" + retrievedRecipe.getName() + "");
                    populateToolbar(retrievedRecipe);
                }
            });
        });
    }


    public void populateToolbar(@NonNull DbRecipe dbRecipe) {
        toolbarPresenter.populateToolbar(this, dbRecipe, toolbar, recipeTitleView,
                yieldDescription, mRecipeImageView, colorPrimaryDark, white);
    }
}

