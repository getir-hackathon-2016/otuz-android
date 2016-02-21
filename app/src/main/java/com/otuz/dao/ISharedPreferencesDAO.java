package com.otuz.dao;

import java.util.Set;

/**
 * Persistence methods for SharedPreferences operations.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public interface ISharedPreferencesDAO {

    void setValue(String key, String value);

    void setValue(String key, Set<String> value);

    void setValue(String key, int value);

    String getValue(String key);

    Set<String> getListValue(String key);

    int getValueInt(String key);

    void removeValue(String key);

    void resetAllSavedData();

}
