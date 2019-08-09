package com.mobile.anvce.baking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.activities.IngredientsListActivity;
import com.mobile.anvce.baking.activities.MainBakingActivity;
import com.mobile.anvce.baking.api.ResourceOverrides;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.viewholders.RecipeViewHolder;

import java.util.List;

import javax.inject.Inject;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> implements BakingAppConstants {

    private final static String TAG = RecipeAdapter.class.getSimpleName();
    private final List<Recipe> list;
    private final Context context;
    private final RequestOptions requestOptions = new RequestOptions().override(300, 300);
    @Inject
    UiDisplayFormat beautifier;
    @Inject
    ResourceOverrides resourceOverridesApi;

    public RecipeAdapter(@NonNull MainBakingActivity context, List<Recipe> list, boolean twoPane) {
        this.list = list;
        this.context = context;
        ((RecipeApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        final Recipe recipe = list.get(position);
        final String recipeName = recipe.getName();
        holder.getNameView().setText(recipeName);
        //		handle the images if present.
        final String imageUrlString = recipe.getImage().isEmpty()
                ? resourceOverridesApi.getRecipeImageOverrideMap().get(recipeName) : recipe.getImage();
        holder.getServingsView().setText(beautifier.formatServings(recipe.getServings()));
        Glide.with(context)
                .load(imageUrlString)
                .apply(requestOptions)
                .into(holder.getRecipeImageView());
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IngredientsListActivity.class);
                intent.putExtra(BakingAppConstants.RECIPE_ID, recipe.getId());
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(context, view);
    }

}

