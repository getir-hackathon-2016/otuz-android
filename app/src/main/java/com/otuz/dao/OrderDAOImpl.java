package com.otuz.dao;

import com.otuz.constant.GeneralValues;
import com.otuz.model.AddressModel;
import com.otuz.model.DAOResponse;
import com.otuz.model.ErrorModel;
import com.otuz.model.ProductModel;
import com.otuz.model.ServerResponseModel;
import com.otuz.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Confirming User's shopping list.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public class OrderDAOImpl implements IOrderDAO{

    @Override
    public DAOResponse confirmShoppingList(String facebookUserId, String deliveryDate) {

        DAOResponse daoResponse = new DAOResponse();

        final HashMap<String, String> functionParameters = new HashMap<>();

        functionParameters.put("facebookUserId" , facebookUserId);
        functionParameters.put("deliveryDate"   , deliveryDate);

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performPostRequest(GeneralValues.BASE_URL + "orders", functionParameters, false);

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

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);

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
