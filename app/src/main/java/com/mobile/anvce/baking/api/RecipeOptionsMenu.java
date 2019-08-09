package com.mobile.anvce.baking.api;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * provides Recipe Options Menu Functionality
 */
public interface RecipeOptionsMenu {
    BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemSelectedListener(@NonNull final AppCompatActivity activity);

    boolean onOptionsItemSelected(@NonNull AppCompatActivity activity, @NonNull MenuItem item);

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    void onPrepareOptionsMenu(@NonNull AppCompatActivity activity, @NonNull Menu menu);


}
