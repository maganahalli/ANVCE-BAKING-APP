package com.mobile.anvce.baking.patterns;

/**
 * Base interface for visitor that servers as marker interface.
 */
public interface AnvceVisitor {
    /**
     * Better than Null. Null are bad.
     * retuns when there is no outout from method.
     */

    Void NOTHING = null;

}
