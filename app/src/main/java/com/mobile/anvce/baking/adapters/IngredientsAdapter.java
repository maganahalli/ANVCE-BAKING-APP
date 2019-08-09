package com.mobile.anvce.baking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.UiDisplayFormat;
import com.mobile.anvce.baking.application.RecipeApplication;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.viewholders.IngredientsViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Manages Adapter to display list of ingredients
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {


    @Inject
    UiDisplayFormat beautifier;
    private final List<Ingredient> ingredients;

    public IngredientsAdapter(@NonNull final Context context, @NonNull final List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        ((RecipeApplication) context.getApplicationContext()).getApplicationComponent().inject(this);

    }


    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        final Ingredient ingredient = ingredients.get(position);
        if (ingredient != null) {
            holder.mIngredientView.setText(beautifier.formatIngredientForDisplay(ingredient));
            holder.itemView.setTag(ingredient);
        }

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
