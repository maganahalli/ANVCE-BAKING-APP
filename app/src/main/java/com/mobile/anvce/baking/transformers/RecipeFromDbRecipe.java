package com.mobile.anvce.baking.transformers;

import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.populators.AnvcePopulatingTransformer;

/**
 * Transforms DbRecipe to Recipe.
 */
public class RecipeFromDbRecipe extends AnvcePopulatingTransformer<DbRecipe, Recipe> {

    @Override
    protected Recipe createTarget() {
        return new Recipe();
    }

    @Override
    protected void populateContents(DbRecipe source, Recipe target) {
        target.setId(source.getRecipeId());
        target.setImage(source.getImage());
        target.setName(source.getName());
        target.setServings(source.getServings());
        target.setIngredients(new RecipeCustomDataConverter().toIngredientList(source.getIngredientsListAsString()));
        target.setSteps(new RecipeCustomDataConverter().toStepsList(source.getStepsListAsString()));
    }
}
