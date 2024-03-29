package com.mobile.anvce.baking.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.adapters.RecipeAdapter;
import com.mobile.anvce.baking.api.ReceipeService;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.BakingMainViewModel;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.network.RetrofitReceipeClient;
import com.mobile.anvce.baking.transformers.RecipeFromDbRecipe;
import com.mobile.anvce.baking.utilities.NetworkUtils;
import com.mobile.anvce.baking.utilities.RecipeDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainBakingActivity extends AppCompatActivity implements BakingAppConstants {

    private static final String TAG = MainBakingActivity.class.getSimpleName();
    private final ReceipeService service = RetrofitReceipeClient.getRetrofitInstance().create(ReceipeService.class);
    private final List<Recipe> mRecipeList = new ArrayList<>();
    @BindView(R.id.recipesListView)
    RecyclerView recipeListView;
    @Inject
    StepsPosition stepsPosition;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBarView;
    private SharedPreferences mPreferences;
    private RecipeDatabaseUtil recipeDatabaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecipeApplication application = (RecipeApplication) this.getApplication();
        application.getApplicationComponent().inject(this);
        stepsPosition.resetStepsAdapterPosition();
        AppDatabase recipeDataBase = AppDatabase.getInstance(getApplicationContext());
        recipeDatabaseUtil = new RecipeDatabaseUtil(getApplicationContext(), recipeDataBase);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        assert recipeListView != null;
        mProgressBarView.setVisibility(View.VISIBLE);
        /* set a GridLayoutManager with default vertical orientation */
        final int spanCount = getResources().getInteger(R.integer.spanCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recipeListView.setLayoutManager(gridLayoutManager);
        setupRecyclerView();
        setupMainBakingViewModel();
    }

    private void setupMainBakingViewModel() {
        BakingMainViewModel viewModel = ViewModelProviders.of(this).get(BakingMainViewModel.class);
        viewModel.getDbRecipeList().observe(this, recipes -> {
            new RecipeFromDbRecipe().transformAll(recipes, mRecipeList);
            String recipeListAsString = new RecipeCustomDataConverter().fromRecipeList(mRecipeList);
            saveRecipeList(recipeListAsString);
        });

    }

    private boolean isOfflineDataAvailable() {
        return !mRecipeList.isEmpty();
    }


    private void setupRecyclerView() {
        restoreSharedPreference();
        if (isOfflineDataAvailable()) {
            buildRecipeList(mRecipeList);
            return;
        } else if (isNetworkAvailable()) {
            downloadRecipeList();
            return;
        }
        buildRecipeList("");
    }

    private boolean isNetworkAvailable() {
        return NetworkUtils.networkStatus(this);
    }

    private void downloadRecipeList() {
        final Call<Recipe> serviceRecipeList = service.getReceipeList();
        serviceRecipeList.enqueue(new Callback<Recipe>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Log.d(TAG, "Recipe service successful");
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    buildRecipeList(response.body().toString());
                }
            }


            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                buildRecipeList("");
            }
        });
    }

    private void buildRecipeList(@NonNull String jsonString) {
        List<Recipe> recipeList = recipeDatabaseUtil.extractRecipeList(jsonString);
        if (!recipeList.isEmpty()) {
            mRecipeList.clear();
            mRecipeList.addAll(recipeList);
            buildRecipeList(mRecipeList);
        }

        recipeDatabaseUtil.storeRecipesToDatabase(recipeList);
    }

    private void buildRecipeList(@NonNull List<Recipe> recipeList) {
        if (!recipeList.isEmpty()) {
            mProgressBarView.setVisibility(View.INVISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(MainBakingActivity.this, recipeList);
            if (recipeListView != null) {
                recipeListView.setAdapter(adapter);
            }
            saveRecipeList(new RecipeCustomDataConverter().fromRecipeList(recipeList));
        }
    }

    private void saveRecipeList(String recipeListAsString) {
        if (TextUtils.isEmpty(recipeListAsString) || mPreferences == null) {
            return;
        }
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(RECIPE_LIST, recipeListAsString);
        preferencesEditor.apply();
    }

    // Restore preferences
    private void restoreSharedPreference() {
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String recipeListAsString = mPreferences.getString(RECIPE_LIST, "");
        if (TextUtils.isEmpty(recipeListAsString)) {
            return;
        }

        List<Recipe> list = new RecipeCustomDataConverter().toRecipeList(recipeListAsString);
        if (!list.isEmpty()) {
            mRecipeList.clear();
            mRecipeList.addAll(list);
        }
    }

}
