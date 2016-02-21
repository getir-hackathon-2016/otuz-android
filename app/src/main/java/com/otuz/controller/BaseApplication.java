package com.otuz.controller;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.otuz.model.UserModel;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Base application class for holding application context and temporary local data and set upping Picasso's caching mechanism.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static UserModel userModel;

    /**
     * Getting Application's Context.
     * @return Application's Context.
     */
    public static Context getAppContext(){
        return BaseApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // Below code block is necessary for adding caching mechanism to Picasso.
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);

    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static void setUserModel(UserModel _userModel) {
        userModel = _userModel;
    }
}