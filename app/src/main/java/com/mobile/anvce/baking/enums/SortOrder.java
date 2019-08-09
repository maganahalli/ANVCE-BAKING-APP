package com.mobile.anvce.baking.enums;

import com.mobile.anvce.baking.patterns.AnvceVisitor;

public enum SortOrder {
    ASCENDING {
        @Override
        public boolean isAscending() {
            return true;
        }
    }, DESCENDING {
        @Override
        public boolean isDescending() {
            return true;
        }
    }, UNKNOWN;

    public static SortOrder fromString(String criteria) {
        return AnvceEnums.fromString(SortOrder.class, criteria, UNKNOWN);
    }

    public interface SortOrderByVisitor<I, O> extends AnvceVisitor {

        O visitAscending(I input);

        O visitDescending(I input);

        O visitUnknown(I input);
    }


    public boolean isAscending() {
        return false;
    }


    public boolean isDescending() {
        return false;
    }

}
