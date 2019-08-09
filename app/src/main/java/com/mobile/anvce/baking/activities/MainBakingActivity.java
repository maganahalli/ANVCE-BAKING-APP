package com.mobile.anvce.baking.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.adapters.RecipeAdapter;
import com.mobile.anvce.baking.api.ReceipeService;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.database.AppDatabase;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.network.RetrofitReceipeClient;
import com.mobile.anvce.baking.utilities.NetworkUtils;
import com.mobile.anvce.baking.utilities.RecipeDatabaseUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainBakingActivity extends AppCompatActivity {

    @BindView(R.id.recipesListView)
    RecyclerView recipeListView;
    @Inject
    StepsPosition stepsPosition;

    private static final String TAG = MainBakingActivity.class.getSimpleName();
    private final ReceipeService service = RetrofitReceipeClient.getRetrofitInstance().create(ReceipeService.class);
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private AppDatabase recipeDataBase;
    private RecipeDatabaseUtil recipeDatabaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecipeApplication application = (RecipeApplication) this.getApplication();
        application.getApplicationComponent().inject(this);
        stepsPosition.resetStepsAdapterPosition();
        recipeDataBase = AppDatabase.getInstance(getApplicationContext());
        recipeDatabaseUtil = new RecipeDatabaseUtil(getApplicationContext(), recipeDataBase);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        assert recipeListView != null;
        /* set a GridLayoutManager with default vertical orientation */
        final int spanCount = getResources().getInteger(R.integer.spanCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recipeListView.setLayoutManager(gridLayoutManager);
        setupRecyclerView();
    }


    private void setupRecyclerView() {
        /**
         if (isNetworkAvailable()) {
         downloadRecipeList();
         return;
         }
         **/
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
                makeText(MainBakingActivity.this, "Recipe service successful", LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    buildRecipeList(response.body().toString());
                }
            }


            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                makeText(MainBakingActivity.this, "Something went wrong...Please try later!", LENGTH_SHORT).show();
                buildRecipeList("");
            }
        });
    }

    private void buildRecipeList(@NonNull String jsonString) {
        List<Recipe> recipeList = recipeDatabaseUtil.extractRecipeList(jsonString);
        if (!recipeList.isEmpty()) {
            RecipeAdapter adapter = new RecipeAdapter(MainBakingActivity.this, recipeList, mTwoPane);
            if (recipeListView != null) {
                recipeListView.setAdapter(adapter);
            }
        }
    }
}
