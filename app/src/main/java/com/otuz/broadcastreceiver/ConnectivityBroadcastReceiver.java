package com.otuz.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.otuz.constant.GeneralValues;
import com.otuz.listener.OnConnectivityTypeChangedListener;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    // Reference to OnConnectivityTypeChangedListener interface.
    private OnConnectivityTypeChangedListener connectivityTypeChangedListener = null;


    @Override
    public void onReceive(Context context, Intent intent) {

        // Set OnNetworkConnectivityChangedListener interface.
        setOnConnectivityTypeChangedListener(context);

        if (connectivityTypeChangedListener != null) {

            // Call interface's methods. So results can be useable in parent Activity.
            connectivityTypeChangedListener.isOnline(isDeviceOnline(context));

        }

    }

    /**
     * Setting OnConnectivityTypeChangedListener interface.
     * @param context Context of an Activity which is registered an instance of this BroadcastReceiver.
     */
    public void setOnConnectivityTypeChangedListener(Context context) {
        try {
            this.connectivityTypeChangedListener = (OnConnectivityTypeChangedListener)context;
        }catch(ClassCastException castException){
            Log.d(GeneralValues.APP_TAG, "ConnectivityBroadcastReceiver - setOnConnectivityTypeChangedListener : error " + castException.toString());
        }
    }

    /**
     * Determine device's internet connection status.
     * @param _context Context of an Activity which is registered an instance of this BroadcastReceiver.
     * @return An boolean response for internet connection status.
     */
    public boolean isDeviceOnline(Context _context){

        ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Return true if and only if there is an active network and this network's current status is "connected".
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

}