package com.mobile.anvce.baking.transformers;

import com.mobile.anvce.baking.database.DbStep;
import com.mobile.anvce.baking.models.Step;
import com.mobile.anvce.baking.populators.AnvcePopulatingTransformer;

/**
 * Transforms MovieDetails to DbMovieDetail.
 */
public class DbStepFromStep extends AnvcePopulatingTransformer<Step, DbStep> {

    @Override
    protected DbStep createTarget() {
        return new DbStep();
    }

    @Override
    protected void populateContents(Step source, DbStep target) {
        target.setDescription(source.getDescription());
        target.setRecipeId(source.getRecipeId());
        target.setStepId(source.getStepId());
        target.setThumbnailURL(source.getThumbnailURL());
        target.setVideoURL(source.getVideoURL());
    }
}
