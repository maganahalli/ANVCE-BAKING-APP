package com.mobile.anvce.baking.api;

import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.viewholders.StepViewHolder;

public interface RecipeStepsOnClickHandler {
    void onClick(StepViewHolder viewHolder, Step step);
}
