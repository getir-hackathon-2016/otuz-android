package com.otuz.dao;

import android.util.Log;

import com.otuz.constant.GeneralValues;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class JSONParseDAOImpl implements IJSONParseDAO {

    @Override
    public boolean getBooleanValue(JSONObject obj, String key){
        boolean result = false;
        try{
            result = obj.getBoolean(key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getBooleanValue : error" + e.toString());
        }
        return result;
    }

    @Override
    public String getStringValue(JSONObject obj, String key){
        String resultText = "";
        try{
            resultText = obj.getString(key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getStringValue : error"+e.toString());
        }
        return resultText;
    }

    @Override
    public int getIntegerValue(JSONObject obj, String key){
        int result = 0;
        try{
            result = obj.getInt(key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getIntegerValue : error : "+e.toString());
        }
        return result;
    }

    @Override
    public JSONArray getArrayFromObject(JSONObject obj, String key){
        JSONArray resultArray = new JSONArray();
        try{
            resultArray = obj.getJSONArray(key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getArrayFromObject : error"+e.toString());
        }
        return resultArray;
    }

    @Override
    public JSONObject getObjectFromArray(JSONArray array, int index){
        JSONObject resultObject = new JSONObject();
        try{
            resultObject = array.getJSONObject(index);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getObjectFromArray : error" + e.toString());
        }
        return resultObject;
    }

    @Override
    public JSONObject getJsonObjectFromObject(JSONObject object, String key){
        JSONObject resultObject = new JSONObject();
        try{
            resultObject = object.getJSONObject(key);
        }catch(Exception e){
            Log.e(GeneralValues.APP_TAG, "JSONParseDAOImpl - getJsonObjectFromObject : error"+e.toString());
        }
        return resultObject;
    }

}
