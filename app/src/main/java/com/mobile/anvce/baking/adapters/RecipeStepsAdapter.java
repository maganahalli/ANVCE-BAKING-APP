package com.mobile.anvce.baking.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.BaseStepsPosition;
import com.mobile.anvce.baking.api.RecipeStepsOnClickHandler;
import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.viewholders.StepViewHolder;

import java.util.ArrayList;

public class RecipeStepsAdapter extends RecyclerView.Adapter<StepViewHolder> {

    final private RecipeStepsOnClickHandler mClickHandler;
    final private Context context;
    private final ArrayList<Step> mSteps;

    public RecipeStepsAdapter(Context context, ArrayList<Step> mSteps, RecipeStepsOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
        this.mSteps = mSteps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        final Step step = mSteps.get(position);
        holder.mShortDescriptionView.setText(step.getShortDescription());
        holder.mAbbreviatedLongDescriptionView.setText(step.getFormattedDescription());
        holder.mStepThumbnailView.setVisibility(step.getThumbnailURL().isEmpty() ? View.GONE : View.VISIBLE);
//			Treating the thumbnails JSON object as images (.png, .jpg), since the project designs for images.
//			Since we can't always expect to consume perfect API, we treat it as an error case.
//			The code falls back and uses the thumbnail as an image.
        Glide.with(holder.getContext()).load(step.getThumbnailURL()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e(getClass().getSimpleName(),
                        String.format("failed to load url: %s with exception %s", step.getThumbnailURL(), e.getMessage()), e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.mStepNumberView.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.mStepThumbnailView);
        holder.mStepNumberView.setText(Integer.toString(position));
        holder.itemView.setBackgroundResource(new BaseStepsPosition(holder.getContext()).getStepsPosition() == position ? R.drawable.touch_selector_selected : R.drawable.touch_selector);
        holder.itemView.setTag(step);
        holder.setClickHandler(mClickHandler);

    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }
}
