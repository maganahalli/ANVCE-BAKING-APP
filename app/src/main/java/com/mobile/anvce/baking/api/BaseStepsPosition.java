package com.mobile.anvce.baking.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.transformers.DbStepFromStep;

import java.util.ArrayList;

public class BaseStepsPosition implements StepsPosition, BakingAppConstants {
    private final Context context;

    public BaseStepsPosition(Context context) {
        this.context = context;
    }

    @Override
    public ArrayList<DbStep> getArrayOfSteps(Bundle arguments) {
        final ArrayList<DbStep> list = new ArrayList<DbStep>();
        if (arguments != null && arguments.containsKey(ARG_STEPS_ARRAY)) {
            final ArrayList<Parcelable> items = arguments.getParcelableArrayList(ARG_STEPS_ARRAY);
            for (Parcelable item : items) {
                if (item instanceof DbStep) {
                    list.add((DbStep) item);
                } else if (item instanceof Step) {
                    Step step = (Step) item;
                    DbStep dStep = new DbStepFromStep().transform(step);
                    list.add(dStep);
                }
            }
        }
        return list;

    }

    @Override
    public int getStepsPosition() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREFS_STEPS_ADAPTER_POSITION, 0);
    }

    @Override
    public void setStepsPosition(int position) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREFS_STEPS_ADAPTER_POSITION, position);
        editor.apply();
    }

    @Override
    public void resetStepsAdapterPosition() {
        setStepsPosition(0);
    }
}
