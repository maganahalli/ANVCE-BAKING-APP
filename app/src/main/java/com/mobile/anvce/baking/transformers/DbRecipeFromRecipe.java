package com.mobile.anvce.baking.transformers;

import com.mobile.anvce.baking.database.DbIngredient;
import com.mobile.anvce.baking.database.DbRecipe;
import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.Recipe;
import com.mobile.anvce.baking.populators.AnvcePopulatingTransformer;

import java.util.List;

/**
 * Transforms Recipe to DbRecipe.
 */
public class DbRecipeFromRecipe extends AnvcePopulatingTransformer<Recipe, DbRecipe> {

    @Override
    protected DbRecipe createTarget() {
        return new DbRecipe();
    }

    @Override
    protected void populateContents(Recipe source, DbRecipe target) {
        target.setRecipeId(source.getId());
        target.setImage(source.getImage());
        target.setName(source.getName());
        target.setServings(source.getServings());
        List<DbIngredient> ingredientList = new DbIngredientFromIngredient().transformAll(source.getIngredients());
        target.setIngredientsListAsString(new RecipeCustomDataConverter().fromIngredientList(ingredientList));
        List<DbStep> stepsList = new DbStepFromStep().transformAll(source.getSteps());
        target.setStepsListAsString(new RecipeCustomDataConverter().fromStepsList(stepsList));
    }
}
