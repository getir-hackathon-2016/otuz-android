package com.otuz.controller;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.otuz.model.UserModel;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static UserModel userModel;

    // A TelephonyManager reference for listening changes on device's data communication technology.
    //private static TelephonyManager telephonyManager;

    /**
     * Getting Application's Context.
     * @return Application's Context.
     */
    public static Context getAppContext(){
        return BaseApplication.context;
    }

    /**
     * Getting Application's TelephonyManager.
     * @return Application's TelephonyManager.
     */
    /*public static TelephonyManager getTelephonyManager(){
        return BaseApplication.telephonyManager;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // Setting TelephonyManager.
        //telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static void setUserModel(UserModel _userModel) {
        userModel = _userModel;
    }
}