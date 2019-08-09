package com.mobile.anvce.baking.transformers;

import com.mobile.anvce.baking.database.DbIngredient;
import com.mobile.anvce.baking.models.Ingredient;
import com.mobile.anvce.baking.populators.AnvcePopulatingTransformer;

/**
 * Transforms MovieDetails to DbMovieDetail.
 */
public class DbIngredientFromIngredient extends AnvcePopulatingTransformer<Ingredient, DbIngredient> {

    @Override
    protected DbIngredient createTarget() {
        return new DbIngredient();
    }

    @Override
    protected void populateContents(Ingredient source, DbIngredient target) {
        target.setMeasure(source.getMeasure());
        target.setQuantity(source.getQuantity());
        target.setIngredient(source.getIngredient());
    }
}
