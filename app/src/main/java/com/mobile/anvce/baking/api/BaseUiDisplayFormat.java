package com.mobile.anvce.baking.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.database.DbIngredient;
import com.mobile.anvce.baking.models.BakingAppConstants;
import com.mobile.anvce.baking.models.Ingredient;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BaseUiDisplayFormat implements BakingAppConstants, UiDisplayFormat {

    private static final NumberFormat INGREDIENT_QUANTITY_FORMAT = new DecimalFormat("##.###");

    private final Context context;

    public BaseUiDisplayFormat(Context context) {
        this.context = context;
    }

    @Override
    public String formatIngredientForDisplay(@NonNull final Ingredient ingredient) {
        StringBuilder stringBuffer = new StringBuilder(String.format("%s ", formatQuantity(ingredient.getQuantity())));
        stringBuffer.append(formatMeasure(ingredient.getMeasure()));
        stringBuffer.append(ingredient.getIngredient());
        return stringBuffer.toString();
    }

    @Override
    public String formatIngredientForDisplay(@NonNull DbIngredient dbIngredient) {
        StringBuilder stringBuffer = new StringBuilder(String.format("%s ", formatQuantity(dbIngredient.getQuantity())));
        stringBuffer.append(formatMeasure(dbIngredient.getMeasure()));
        stringBuffer.append(dbIngredient.getIngredient());
        return stringBuffer.toString();

    }

    private String formatMeasure(@NonNull final String measure) {
        return "UNIT".equalsIgnoreCase(measure) ? "" : String.format("%s ", measure.toLowerCase(Locale.US));
    }

    private String formatQuantity(final double quantity) {
        return INGREDIENT_QUANTITY_FORMAT.format(quantity);
    }

    @Override
    public String formatServings(int servings) {
        return formatServings(context.getString(R.string.servings), servings);
    }

    @Override
    public String formatServings(@NonNull String servingsFormat, int servings) {
        return String.format(servingsFormat, Integer.toString(servings));
    }

}

