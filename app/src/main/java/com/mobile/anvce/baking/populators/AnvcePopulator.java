package com.mobile.anvce.baking.populators;

/**
 * API for mapping the values from a source object to a target object.
 *
 * @param <S> Source Type, for example: MovieDetail
 * @param <T> Target Type, for example: DbMovieDetail
 * @author Venky maganahalli
 */
public interface AnvcePopulator<S, T> {

    /**
     * Copy the values from the source to the target.
     *
     * @param source the original object, must not be null
     * @param target to update with values from the original, must not be null
     */
    void populate(S source, T target);
}

