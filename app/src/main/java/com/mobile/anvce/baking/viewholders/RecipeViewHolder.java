package com.mobile.anvce.baking.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Cache of the children views for a forecast list item.
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final static String TAG = RecipeViewHolder.class.getSimpleName();

    private final Context mContext;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.name)
    TextView mNameView;
    @BindView(R.id.recipeImage)
    ImageView mRecipeImageView;
    @BindView(R.id.serving)
    TextView mServingsView;

    public RecipeViewHolder(Context context, View view) {
        super(view);
        ButterKnife.bind(this, view);
        mContext = context;
        mCardView.setOnClickListener(this);
    }

    public TextView getNameView() {
        return mNameView;
    }

    public ImageView getRecipeImageView() {
        return mRecipeImageView;
    }

    public TextView getServingsView() {
        return mServingsView;
    }

    public CardView getCardView() {
        return mCardView;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "clicked me");
    }
}