package com.otuz.dao;

import android.util.Log;

import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.otuz.constant.GeneralValues;
import com.otuz.model.DAOResponse;
import com.otuz.model.ErrorModel;
import com.otuz.model.MapLocationModel;
import com.otuz.model.ServerResponseModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Responsible for sending lat/long to Google and get that points address.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public class MapDAOImpl implements IMapDAO{

    private static final String GOOGLE_MAPS_FORMATTED_ADDRESS_BASE_URL  = "https://maps.google.com/maps/api/geocode/json?latlng=";
    private static final String GOOGLE_MAPS_CO_URL                      = "&sensor=false&language=" + "tr";

    @Override
    public DAOResponse getFormattedAddressFromGoogleMaps(double _latitude, double _longitude){

        DAOResponse daoResponse = new DAOResponse();

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performGetRequest(GOOGLE_MAPS_FORMATTED_ADDRESS_BASE_URL + _latitude + "," + _longitude + GOOGLE_MAPS_CO_URL, false);

            if(serverResponse.isSuccess()){
                // There is not any error on server connection, let's parse the api error and data.

                IJSONParseDAO jsonParseDAO = new JSONParseDAOImpl();
                ErrorModel error = new ErrorModel();

                JSONObject responseAsJsonObject = new JSONObject(serverResponse.getResponse());

                // If there isn't any error ("Error": null in JSON) below JSON parse operations will be catched in jsonParseDAO. So errorCode will be equals to 0 (zero).
                // Long story short if there isn't an error in the returned string, error code will be zero.
                JSONObject errorJSONObject  = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "error");
                int errorCode               = jsonParseDAO.getIntegerValue  (errorJSONObject, "code");

                error.setErrorCode(errorCode);

                // Parsing Product Data.
                MapLocationModel mapLocationModel = new MapLocationModel();

                JSONArray resultJSONArray   = jsonParseDAO.getArrayFromObject(responseAsJsonObject, "results");
                JSONObject addressResultJSON = (JSONObject) resultJSONArray.get(0);
                String formattedAddress     = jsonParseDAO.getStringValue(addressResultJSON, "formatted_address");

                // Setting up model with parsed data.
                mapLocationModel.setFormattedAddress(formattedAddress);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(mapLocationModel);

            }else{
                // Http status code 1xx - 3xx - 4xx - 5xx.
                ErrorModel error = new ErrorModel();
                error.setErrorCode(serverResponse.getCode());
                error.setErrorMessage(serverResponse.getResponse());
                daoResponse.setError(error);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return daoResponse;

    }

}
