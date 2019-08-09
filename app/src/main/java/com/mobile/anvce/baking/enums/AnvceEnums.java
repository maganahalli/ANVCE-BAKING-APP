package com.mobile.anvce.baking.enums;

/**
 * Utility class like java.util.Collections that assists with enums.
 *
 * @author Venky maganahalli
 */
public class AnvceEnums {

    /**
     * Returns the constant with the specified name of the specified enum type
     * or the default value.
     *
     * @param enumType     the class of the enumerated type to search for the
     *                     constant value.
     * @param name         the name of the constant value to find.
     * @param defaultValue to use when enum is not found for supplied name
     * @return the enum constant.
     */
    public static <T extends Enum<T>> T fromString(Class<T> enumType, String name, T defaultValue) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (Exception exception) {
            return defaultValue;
        }
    }

}
