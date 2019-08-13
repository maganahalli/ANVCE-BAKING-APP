package com.mobile.anvce.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mobile.anvce.baking.R;
import com.mobile.anvce.baking.database.RecipeCustomDataConverter;
import com.mobile.anvce.baking.models.Ingredient;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // Creates and returns a ListRemoteViewsFactory
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private List<Ingredient> mIngredientList;

        public ListRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        // called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            // Get the updated ingredient string from shared preferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String ingredientString = sharedPreferences.getString(getString(R.string.pref_ingredient_list_key), "");

            // Convert ingredient string to the list of ingredients
            mIngredientList = new RecipeCustomDataConverter().toIngredientList(ingredientString);
        }

        @Override
        public void onDestroy() {

        }

        /**
         * Returns the number of items to be displayed in the ListView widget
         */
        @Override
        public int getCount() {
            if (mIngredientList == null) return 0;
            return mIngredientList.size();
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter.
         *
         * @param position The current position of the item in the GridView to be displayed
         * @return The RemoteViews object to display for the provided position
         */
        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredientList == null || mIngredientList.size() == 0) return null;

            Ingredient ingredient = mIngredientList.get(position);
            // Extract the ingredient details
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

            views.setTextViewText(R.id.widget_quantity, String.valueOf(quantity));
            views.setTextViewText(R.id.widget_measure, measure);
            views.setTextViewText(R.id.widget_ingredient, ingredientName);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1; // Treat all items in the ListView the same
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}

