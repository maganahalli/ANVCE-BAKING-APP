package com.mobile.anvce.baking.api;

import com.mobile.anvce.baking.models.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit Service Contract and route declaration.
 */

public interface ReceipeService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<Recipe> getReceipeList();

}
