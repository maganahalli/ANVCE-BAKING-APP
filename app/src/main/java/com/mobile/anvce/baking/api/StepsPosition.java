package com.mobile.anvce.baking.api;

import android.os.Bundle;

import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.models.Step;

import java.util.ArrayList;

public interface StepsPosition {
    ArrayList<DbStep>  getArrayOfSteps(Bundle arguments);

    int getStepsPosition();

    void setStepsPosition(int position);

    void resetStepsAdapterPosition();
}
