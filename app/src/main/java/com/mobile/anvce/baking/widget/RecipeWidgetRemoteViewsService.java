package com.mobile.anvce.baking.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by  Venkatesh Maganahalli
 */

public class RecipeWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(getClass().getSimpleName(), "onGetViewFactory: " + "Service called");
        return new RecipeWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
