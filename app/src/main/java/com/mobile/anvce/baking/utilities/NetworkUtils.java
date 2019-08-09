package com.mobile.anvce.baking.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * These utilities will be used to communicate with the network.
 */
public final class NetworkUtils {

    /**
     * Determine network status
     *
     * @param context - application context
     * @return boolean true or false
     */
    public static Boolean networkStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
