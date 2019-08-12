package com.mobile.anvce.baking.api;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.models.Recipe;

import java.util.Objects;

/**
 * Implementation of the Recipe Toolbar
 */
public class BaseRecipeToolBar implements RecipeToolBar {


    private static final RequestOptions REQUEST_OPTIONS = new RequestOptions().override(300, 300);
    private final UiDisplayFormat beautifier;
    private final ResourceOverrides resourceOverrides;

    public BaseRecipeToolBar(@NonNull UiDisplayFormat beautifier, @NonNull ResourceOverrides resourceOverrides) {
        this.beautifier = beautifier;
        this.resourceOverrides = resourceOverrides;
    }

    @Override
    public void populateToolbar(@NonNull AppCompatActivity activity, @NonNull DbRecipe recipe, @NonNull Toolbar toolbar, @NonNull TextView recipeTitleView, @NonNull TextView yieldDescription, @Nullable ImageView mRecipeImageView, int backgroundColor, int titleTextColor) {
        final String recipeName = recipe.getName();
        toolbar.setTitle(recipeName);
        toolbar.setBackgroundColor(backgroundColor);
        toolbar.setTitleTextColor(titleTextColor);
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setText(recipeTitleView, recipeName);
//		handle the images if present.
        final String imageUrlString = TextUtils.isEmpty(recipe.getImage())
                ? resourceOverrides.getRecipeImageOverrideMap().get(recipeName) : recipe.getImage();
        setText(yieldDescription, beautifier.formatServings(recipe.getServings()));
        if (mRecipeImageView != null) {
            Glide.with(activity)
                    .load(imageUrlString)
                    .apply(REQUEST_OPTIONS)
                    .into(mRecipeImageView);
        }

    }


    private void setText(TextView view, final String text) {
        if (view == null) {
            return;
        }
        view.setText(text);

    }


}
