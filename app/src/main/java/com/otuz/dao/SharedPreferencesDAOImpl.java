package com.otuz.dao;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.otuz.constant.GeneralValues;
import com.otuz.controller.BaseApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles locally stored SharedPreferences data with its own try-catch mechanism.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public class SharedPreferencesDAOImpl implements ISharedPreferencesDAO{

    @Override
    public void setValue(String key, String value) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
            Log.d(GeneralValues.APP_TAG, "PreferencesDao - setValue(String key, String value) - Newly Setted Key/Value : " + key + "/" + value);
        }catch (Exception e) {
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - setValue(String key, String value) : " + e.toString());
        }
    }

    @Override
    public void setValue(String key, Set<String> value) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(key, value);
            editor.commit();
            Log.d(GeneralValues.APP_TAG,"PreferencesDao - setValue(String key, HashSet<String> value) - Newly Setted Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - setValue(String key, HashSet<String> value) : " + e.toString());
        }
    }

    @Override
    public void setValue(String key, int value) {
        try{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
            Log.d(GeneralValues.APP_TAG,"PreferencesDao - setValue(String key, int value) - Newly Setted Key/Value : " + key + "/" + value);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - setValue(String key, int value) : " + e.toString());
        }
    }

    @Override
    public String getValue(String key) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        String value = mSharedPrefs.getString(key, "");
        mPrefsEditor.commit();
        Log.d(GeneralValues.APP_TAG,"PreferencesDao - String getValue(String key) - Getted Key/Value : " + key + "/" + value);
        return value;
    }

    @Override
    public Set<String> getListValue(String key) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        Set<String> value = mSharedPrefs.getStringSet(key, new HashSet<String>());
        mPrefsEditor.commit();
        Log.d(GeneralValues.APP_TAG,"PreferencesDao - HashSet<String> getValue(String key) - Getted Key/Value : " + key + "/" + value);
        return value;
    }

    @Override
    public int getValueInt(String key) {
        int value = 0;
        try{
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
            value = mSharedPrefs.getInt(key, 0);
            mPrefsEditor.commit();
            Log.d(GeneralValues.APP_TAG,"PreferencesDao - int getValue(String key) - Getted Key/Value : " + key + "/" + value);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - int getValue(String key) : " + e.toString());
        }
        return value;
    }

    @Override
    public void removeValue(String key) {
        try{
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            mSharedPrefs.edit().remove(key).commit();
            Log.d(GeneralValues.APP_TAG,"PreferencesDao - removeValue(String key) - Removed Key : " + key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - removeValue() : " + e.toString());
        }
    }

    @Override
    public void resetAllSavedData(){
        try{
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
            mSharedPrefs.edit().clear().commit();
            Log.d(GeneralValues.APP_TAG,"PreferencesDao - resetAllSavedData() - Cleared ");
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG,"PreferencesDao - resetAllSavedData() : " + e.toString());
        }
    }

}
