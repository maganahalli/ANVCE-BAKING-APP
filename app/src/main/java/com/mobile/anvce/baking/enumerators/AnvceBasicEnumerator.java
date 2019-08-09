package com.mobile.anvce.baking.enumerators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helps avoid conditional logic in business code.
 *
 * @author Venky Maganahalli
 */
public enum AnvceBasicEnumerator implements AnvceEnumerator {

    DEFAULT;

    @Override
    public <I, C extends Collection<? extends I>> boolean anySatisfy(C items, AnvceMatcher<I, Number> matcher) {
        for (I item : items) {
            if (matcher.isMatch(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T coalesce(T primary, T alternate) {
        return primary != null ? primary : alternate;
    }

    @Override
    public <I, IC extends Collection<? extends I>> List<I> select(IC items, AnvceMatcher<? super I, Number> matcher) {
        List<I> results = new ArrayList<>();
        for (I item : items) {
            if (matcher.isMatch(item)) {
                results.add(item);
            }
        }
        return results;
    }
}