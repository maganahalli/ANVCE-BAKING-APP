package com.mobile.anvce.baking.api;

import java.util.Map;

public interface ResourceOverrides {

    Map<String, Integer> getRecipeIconOverrideMap();

    Map<String, String> getRecipeImageOverrideMap();
}
