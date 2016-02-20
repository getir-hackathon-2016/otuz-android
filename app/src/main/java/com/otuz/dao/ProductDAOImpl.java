package com.otuz.dao;

import android.util.Log;

import com.otuz.constant.GeneralValues;
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
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ProductDAOImpl implements IProductDAO{

    @Override
    public DAOResponse getProductViaBarcodeNumber(String barcodeNumber) {

        DAOResponse daoResponse = new DAOResponse();

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performGetRequest(GeneralValues.BASE_URL + "products/" + barcodeNumber, false);

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
                ProductModel productModel = new ProductModel();

                JSONObject productJSONObject    = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String productId                = jsonParseDAO.getStringValue(productJSONObject, "_id");
                String productName              = jsonParseDAO.getStringValue(productJSONObject, "name");
                String productPhoto             = jsonParseDAO.getStringValue(productJSONObject, "photoUrl");
                String barcodeNo                = jsonParseDAO.getStringValue(productJSONObject, "barcodeNumber");
                String price                    = jsonParseDAO.getStringValue(productJSONObject, "price");
                String quantity                 = jsonParseDAO.getStringValue(productJSONObject, "quantity");

                // Setting up model with parsed data.
                productModel.setProductId(productId);
                productModel.setName(productName);
                productModel.setPhotoUrl(productPhoto);
                productModel.setBarcodeNumber(barcodeNo);
                productModel.setPrice(price);
                productModel.setQuantity(quantity);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(productModel);

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
