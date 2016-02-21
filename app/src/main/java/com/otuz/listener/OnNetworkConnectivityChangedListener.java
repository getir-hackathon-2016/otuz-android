package com.otuz.listener;

/**
 * An interface between ConnectivityBroadcastReceiver and the Activity that will check for device's online status.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface OnNetworkConnectivityChangedListener {

    /**
     * Get current internet connectivity status.
     * @param isOnline A boolean stands for internet connectivity status.
     */
    void isOnline(boolean isOnline);

}