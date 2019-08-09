package com.mobile.anvce.baking.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.mobile.anvce.baking.models.BakingAppConstants;

public class RecipeWidgetBroadcastReceiver extends BroadcastReceiver implements BakingAppConstants {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOG_TAG);
        wakeLock.acquire();

        RecipeWidgetProvider.sendRefreshBroadcast(context);

        //Release the lock
        wakeLock.release();
    }
}
