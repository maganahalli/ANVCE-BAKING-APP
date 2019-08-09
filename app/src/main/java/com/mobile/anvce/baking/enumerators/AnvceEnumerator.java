package com.mobile.anvce.baking.enumerators;

import java.util.Collection;
import java.util.List;

/**
 * API to iterate over items.
 *
 * @author Venky Maganahalli
 */
public interface AnvceEnumerator {

    /**
     * Answer whether at least one of the supplied items matches a condition.
     *
     * @param items   that might match the condition, must not be null
     * @param matcher the condition to match, must not be null
     * @return true/false
     */
    <I, C extends Collection<? extends I>> boolean anySatisfy(C items, AnvceMatcher<I, Number> matcher);

    /**
     * Return the first non null parameter, if no parameters are non null then
     * null will be returned.
     * <p>
     * The name coalesce is used for the same operation in some databases like
     * oracle.
     *
     * @param <T>       the type of data being coalesced
     * @param primary   will be returned if not null
     * @param alternate to use if primary is null
     * @return primary or alternate, may be null
     */
    <T> T coalesce(T primary, T alternate);


    /**
     * Gather a list of elements that match a supplied criteria.
     *
     * @param items   to be filtered
     * @param matcher filters the items
     * @return a collection of items that satisfy the matcher
     */
    <I, IC extends Collection<? extends I>> List<I> select(IC items, AnvceMatcher<? super I, Number> matcher);


}
