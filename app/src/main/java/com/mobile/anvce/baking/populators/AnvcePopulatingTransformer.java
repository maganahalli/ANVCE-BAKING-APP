package com.mobile.anvce.baking.populators;

import com.mobile.anvce.baking.transformers.AnvceBaseTransformer;

import java.util.List;

/**
 * Provides basic behavior to support object transformations that involve
 * mapping the contents of the original to the target.
 * <p>
 * This class itself is thread-safe but you must ensure the source is not being
 * modified on another thread and the target is not exposed to another thread.
 * For example: do not run this transformer on the background to update a model
 * that can already be accessed by the UI thread. For this reason model objects
 * are typically only populated in the UI thread, although it is OK to create
 * them in background threads.
 *
 * @param <O> type of original object for example: MovieDetail
 * @param <T> type of transformed object for example: DbMovieDetail
 * @author Venky maganahalli
 */
public abstract class AnvcePopulatingTransformer<O, T> extends AnvceBaseTransformer<O, T> implements AnvcePopulator<O, T> {

    @Override
    public final T convert(O original) {
        T target = createTarget();
        populate(original, target);
        return target;
    }

    /**
     * Create a new instance that is the target of the transformation.
     *
     * @return a new instance, never null
     */
    protected abstract T createTarget();

    @Override
    protected T defaultTransformation() {
        return createTarget();
    }

    /**
     * Set the contents of the source collection to point to the contents of the
     * target collection.
     * <p>
     * The elements of the target collection will be the same instances as the
     * elements in the source collection.
     * <p>
     * This is intended to be used by immutable elements such as String,
     * Integer...
     *
     * @param source collection to mimic, must not be null
     * @param target collection to update, must not be null
     */
    protected <E> void matchContents(List<E> source, List<E> target) {
        target.clear();
        target.addAll(source);
    }

    @Override
    public final void populate(O source, T target) {
        if (source != null) {
            populateContents(source, target);
        }
    }

    /**
     * Copy the values from the source to the target.
     *
     * @param source the original object, must not be null
     * @param target to update with values from the original, must not be null
     */
    protected abstract void populateContents(O source, T target);

    /**
     * TODO explore the need to use this, ambiguous input parameters should be
     * challenged.
     * <p>
     * Right now we are using this to deal with fields in the schema that are
     * typed as Object - we should get isis to change these.
     *
     * @param value an object
     * @return a string
     */
    protected String toStringFromObjectField(Object value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Convert the supplied object to a String or null.
     *
     * @param value an object, Big Integer..., or null
     * @return a string, or null
     */
    protected String toStringOrNull(Object value) {
        return value == null ? null : value.toString();
    }

}