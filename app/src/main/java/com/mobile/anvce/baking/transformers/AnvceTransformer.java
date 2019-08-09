package com.mobile.anvce.baking.transformers;

import java.util.Collection;
import java.util.List;

/**
 * API for creating a new transformed object, based on the supplied original
 * object.
 *
 * @param <O> type of original object for example: MovieDetail
 * @param <T> type of transformed object for example: DbMovieDetail
 * @author Venky maganahalli
 */
public interface AnvceTransformer<O, T> {

    /**
     * Create a new transformed object, based on the supplied original object.
     * <p>
     * Implementers are responsible for dealing with null originals, typically
     * implementations will simply return null.
     *
     * @param original the subject of the transformation, may be null
     * @return the transformed object, may be null
     */
    T transform(O original);

    /**
     * Take a collection of source objects, convert each object, and return a
     * list with the converted objects. The order of the inputItems will be
     * preserved.
     *
     * @param inItems a collection of source elements, may be null
     * @return a list of transformed items
     */
    List<T> transformAll(Collection<O> inItems);

    /**
     * Take a list of source objects, convert each object, and fill another list
     * with the converted objects.
     *
     * @param inItems  a collection of source elements, may be null
     * @param outItems the collection to fill, must not be null
     */
    void transformAll(Collection<O> inItems, Collection<T> outItems);

    /**
     * Take an array source objects, convert each object, and fill a list with
     * the converted objects.
     *
     * @param inItems  an array of input items, may be null
     * @param outItems the collection to fill, must not be null
     */
    void transformAll(O[] inItems, Collection<T> outItems);

}

