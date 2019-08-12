package com.mobile.anvce.baking.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.api.BaseStepsPosition;
import com.mobile.anvce.baking.api.RecipeStepsOnClickHandler;
import com.mobile.anvce.baking.api.StepsPosition;
import com.mobile.anvce.baking.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.description)
    public TextView mAbbreviatedLongDescriptionView;
    @BindView(R.id.shortDescription)
    public TextView mShortDescriptionView;
    @BindView(R.id.stepNumber)
    public TextView mStepNumberView;
    @BindView(R.id.stepThumbnail)
    public ImageView mStepThumbnailView;
    StepsPosition stepsPosition;
    private Context context;
    private RecipeStepsOnClickHandler mClickHandler;

    public StepViewHolder(Context context, View view) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
        this.context = context;
        stepsPosition = new BaseStepsPosition(context);
    }

    public Context getContext() {
        return context;
    }

    public void setClickHandler(RecipeStepsOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @Override
    public void onClick(View view) {
        stepsPosition.setStepsPosition(getAdapterPosition());
        Step step = (Step) view.getTag();
        mClickHandler.onClick(this, step);
    }
}
