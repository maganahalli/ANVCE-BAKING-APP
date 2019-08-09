package com.mobile.anvce.baking.transformers;

import com.mobile.anvce.baking.enumerators.AnvceBasicEnumerator;
import com.mobile.anvce.baking.enumerators.AnvceEnumerator;
import com.mobile.anvce.baking.enumerators.AnvceMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides basic behavior to support object transformations.
 * <p>
 * This class is thread safe.
 *
 * @param <O> type of original object for example: MovieDetail
 * @param <T> type of transformed object for example: DbMovieDetail
 * @author Venky maganahalli
 */
public abstract class AnvceBaseTransformer<O, T> implements AnvceTransformer<O, T> {

    private final AnvceEnumerator enumerator = AnvceBasicEnumerator.DEFAULT;

    /**
     * Trim and add the supplied string to the collection if it is not blank.
     *
     * @param strings to update
     * @param string  a string or null
     */
    protected void addNonBlank(Collection<String> strings, String string) {
        String trimmed = ensureNotNull(string).trim();
        if (trimmed.length() > 0) {
            strings.add(trimmed);
        }
    }

    /**
     * Answer whether at least one of the supplied items matches a condition.
     *
     * @param items   that might match the condition, must not be null
     * @param matcher the condition to match, must not be null
     * @return true/false
     */
    protected <I, C extends Collection<? extends I>> boolean anySatisfy(C items, AnvceMatcher<I, Number> matcher) {
        return enumerator.anySatisfy(items, matcher);
    }

    /**
     * Return the first non null parameter, if no parameters are non null then
     * null will be returned.
     * <p>
     * The name coalesce is used for the same operation in some databases like
     * oracle.
     *
     * @param primary   will be returned if not null
     * @param alternate to use if primary is null
     * @return primary or alternate, may be null
     * @see <a href="http://docs.oracle.com/javadb/10.8.2.2/ref/rreffunccoalesce.html"/>
     */
    protected <I> I coalesce(I primary, I alternate) {
        return enumerator.coalesce(primary, alternate);
    }

    /**
     * Create a new transformed object, based on the supplied original object.
     * <p>
     * Implementers do not need to worry about the original being null.
     *
     * @param original the subject of the transformation, must not be null
     * @return the transformed object, will not be null
     */
    protected abstract T convert(O original);

    /**
     * Return the value to use when the original is null.
     * <p>
     * Subclasses should override this to provide empty strings and empty
     * collections when possible.
     *
     * @return the transformed object, typically null, "", empty collection...
     */
    protected T defaultTransformation() {
        return null;
    }

    /**
     * Returns the original string or an empty string, never null.
     *
     * @param value may be null
     * @return value or empty string, never null
     */
    protected String ensureNotNull(String value) {
        return coalesce(value, "");
    }

    /**
     * Gather a list of elements that match a supplied criteria.
     *
     * @param items   to be filtered
     * @param matcher filters the items
     * @return a collection of items that satisfy the matcher
     */
    protected <I, IC extends Collection<? extends I>> List<I> select(IC items, AnvceMatcher<? super I, Number> matcher) {
        return enumerator.select(items, matcher);
    }

    @Override
    public final T transform(O original) {
        return original == null ? defaultTransformation() : convert(original);
    }

    @Override
    public List<T> transformAll(Collection<O> inItems) {
        List<T> outItems = new ArrayList<>(inItems.size());
        transformAll(inItems, outItems);
        return outItems;
    }

    @Override
    public void transformAll(Collection<O> inItems, Collection<T> outItems) {
        if (inItems != null) {
            transformCollection(inItems, outItems);
        }
    }

    @Override
    public void transformAll(O[] inItems, Collection<T> outItems) {
        if (inItems != null) {
            transformArray(inItems, outItems);
        }
    }

    /**
     * Take an array source objects, convert each object, and fill a list with
     * the converted objects.
     *
     * @param inItems  an array of input items, must not be null
     * @param outItems the collection to fill, must not be null
     */
    protected void transformArray(O[] inItems, Collection<T> outItems) {
        for (O inItem : inItems) {
            T outItem = transform(inItem);
            outItems.add(outItem);
        }
    }

    /**
     * Take a list of source objects, convert each object, and fill another list
     * with the converted objects.
     *
     * @param inItems  a collection of source elements, must not be null
     * @param outItems the collection to fill, must not be null
     */
    protected void transformCollection(Collection<O> inItems, Collection<T> outItems) {
        outItems.clear();
        for (O inItem : inItems) {
            T outItem = transform(inItem);
            outItems.add(outItem);
        }
    }

}
