package com.otuz.dao;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Persistence methods for JSON parse operations.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IJSONParseDAO {

    /**
     * Get boolean value from a JSON object with given key.
     * @param obj JSON object which holds the desired boolean value.
     * @param key Key for desired boolean value.
     * @return Desired boolean value.
     */
    boolean getBooleanValue(JSONObject obj, String key);

    /**
     * Get string value from a JSON object with given key.
     * @param obj JSON object which holds the desired string value.
     * @param key Key for desired string value.
     * @return Desired string value.
     */
    String getStringValue(JSONObject obj, String key);

    /**
     * Get integer value from a JSON object with given key.
     * @param obj JSON object which holds the desired integer value.
     * @param key Key for desired integer value.
     * @return Desired integer value.
     */
    int getIntegerValue(JSONObject obj, String key);

    /**
     * Get a JSON array from a JSON object with given key.
     * @param obj JSON object which holds the desired JSON array.
     * @param key Key for desired JSON array.
     * @return Desired JSON array.
     */
    JSONArray getArrayFromObject(JSONObject obj, String key);

    /**
     * Get a JSON object from a JSON array with given index.
     * @param arr JSON array which holds the desired JSON object.
     * @param index Index of desired JSON object in given JSON array.
     * @return Desired JSON object.
     */
    JSONObject getObjectFromArray(JSONArray arr, int index);

    /**
     * Get a JSON object from a JSON object with given key.
     * @param obj JSON object which holds the desired JSON object.
     * @param key Key for desired JSON object.
     * @return Desired JSON object.
     */
    JSONObject getJsonObjectFromObject(JSONObject obj, String key);

}
