package com.otuz.dao;

import com.google.gson.Gson;
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
 * Responsible for handling user data operations end-points.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class UserDAOImpl implements IUserDAO {

    @Override
    public DAOResponse getUser(String facebookUserId) {

        DAOResponse daoResponse = new DAOResponse();

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performGetRequest(GeneralValues.BASE_URL + "users/" + facebookUserId, false);

            if(serverResponse.isSuccess()){
                // There is not any error on server connection, let's parse the api error and data.

                IJSONParseDAO jsonParseDAO = new JSONParseDAOImpl();
                ErrorModel error = new ErrorModel();

                JSONObject responseAsJsonObject = new JSONObject(serverResponse.getResponse());

                // If there isn't any error ("Error": null in JSON) below JSON parse operations will be catched in jsonParseDAO. So errorCode will be equals to 0 (zero).
                // Long story short if there isn't an error in the returned string, error code will be zero.
                JSONObject errorJSONObject = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "error");
                int errorCode       = jsonParseDAO.getIntegerValue  (errorJSONObject, "code");

                error.setErrorCode(errorCode);

                // Parsing User Data.
                UserModel userModel = new UserModel();

                JSONObject userJSONObject   = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String userId               = jsonParseDAO.getStringValue(userJSONObject, "_id");
                String fbUserId             = jsonParseDAO.getStringValue(userJSONObject, "facebookUserId");
                String registeredAt         = jsonParseDAO.getStringValue(userJSONObject, "registeredAt");

                // Parsing User Products.
                ArrayList<ProductModel> productModels = new ArrayList<>();
                JSONArray userProductsArray = jsonParseDAO.getArrayFromObject(userJSONObject, "products");
                for(int i = 0; i < userProductsArray.length(); i++){

                    ProductModel productModel   = new ProductModel();
                    JSONObject productAsJSON    = (JSONObject)userProductsArray.get(i);

                    productModel.setName            (jsonParseDAO.getStringValue(productAsJSON,"name"));
                    productModel.setPhotoUrl(jsonParseDAO.getStringValue(productAsJSON, "photoUrl"));
                    productModel.setPrice(jsonParseDAO.getStringValue(productAsJSON, "price"));
                    productModel.setQuantity(jsonParseDAO.getStringValue(productAsJSON, "quantity"));
                    productModel.setBarcodeNumber(jsonParseDAO.getStringValue(productAsJSON, "barcodeNumber"));
                    productModel.setProductId(jsonParseDAO.getStringValue(productAsJSON,"_id"));

                    productModels.add(productModel);

                }

                AddressModel addressModel           = new AddressModel();
                JSONObject userAddressJSONObject    = jsonParseDAO.getJsonObjectFromObject(userJSONObject, "address");

                addressModel.setAddress(jsonParseDAO.getStringValue(userAddressJSONObject, "address"));
                addressModel.setBuildingNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "buildingNumber"));
                addressModel.setDoorNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "doorNumber"));
                addressModel.setLandmark(jsonParseDAO.getStringValue(userAddressJSONObject, "landmark"));

                String userLatitude = jsonParseDAO.getStringValue(userAddressJSONObject, "latitude");
                double userLatDouble;
                if(userLatitude.equals(""))
                    userLatDouble = 0;
                else
                    userLatDouble = Double.parseDouble(userLatitude);

                String userLongitude = jsonParseDAO.getStringValue(userAddressJSONObject, "longitude");
                double userLngDouble;
                if(userLongitude.equals(""))
                    userLngDouble = 0;
                else
                    userLngDouble = Double.parseDouble(userLongitude);

                addressModel.setLatitude(userLatDouble);
                addressModel.setLongitude(userLngDouble);

                // Setting up model with parsed data.
                userModel.setId(userId);
                userModel.setFacebookUserId(fbUserId);
                userModel.setRegisteredAt(registeredAt);
                userModel.setUserProducts(productModels);
                userModel.setAddressModel(addressModel);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(userModel);

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

    @Override
    public DAOResponse saveUser(String facebookUserId) {

        DAOResponse daoResponse = new DAOResponse();

        final HashMap<String, String> functionParameters = new HashMap<>();

        functionParameters.put("facebookUserId" , facebookUserId);

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performPostRequest(GeneralValues.BASE_URL + "users", functionParameters, false);

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

                // Parsing User Data.
                UserModel userModel = new UserModel();

                JSONObject userJSONObject   = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String userId               = jsonParseDAO.getStringValue(userJSONObject, "_id");
                String fbUserId             = jsonParseDAO.getStringValue(userJSONObject, "facebookUserId");
                String registeredAt         = jsonParseDAO.getStringValue(userJSONObject, "registeredAt");

                // Parsing User Products.
                ArrayList<ProductModel> productModels = new ArrayList<>();
                JSONArray userProductsArray = jsonParseDAO.getArrayFromObject(userJSONObject, "products");
                for(int i = 0; i < userProductsArray.length(); i++){

                    ProductModel productModel   = new ProductModel();
                    JSONObject productAsJSON    = (JSONObject)userProductsArray.get(i);

                    productModel.setName(jsonParseDAO.getStringValue(productAsJSON, "name"));
                    productModel.setPhotoUrl(jsonParseDAO.getStringValue(productAsJSON, "photoUrl"));
                    productModel.setPrice(jsonParseDAO.getStringValue(productAsJSON, "price"));
                    productModel.setQuantity(jsonParseDAO.getStringValue(productAsJSON, "quantity"));
                    productModel.setBarcodeNumber(jsonParseDAO.getStringValue(productAsJSON, "barcodeNumber"));
                    productModel.setProductId(jsonParseDAO.getStringValue(productAsJSON, "_id"));

                    productModels.add(productModel);

                }

                AddressModel addressModel           = new AddressModel();
                JSONObject userAddressJSONObject    = jsonParseDAO.getJsonObjectFromObject(userJSONObject, "address");

                addressModel.setAddress(jsonParseDAO.getStringValue(userAddressJSONObject, "address"));
                addressModel.setBuildingNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "buildingNumber"));
                addressModel.setDoorNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "doorNumber"));
                addressModel.setLandmark(jsonParseDAO.getStringValue(userAddressJSONObject, "landmark"));

                String userLatitude = jsonParseDAO.getStringValue(userAddressJSONObject, "latitude");
                double userLatDouble;
                if(userLatitude.equals(""))
                    userLatDouble = 0;
                else
                    userLatDouble = Double.parseDouble(userLatitude);

                String userLongitude = jsonParseDAO.getStringValue(userAddressJSONObject, "longitude");
                double userLngDouble;
                if(userLongitude.equals(""))
                    userLngDouble = 0;
                else
                    userLngDouble = Double.parseDouble(userLongitude);

                addressModel.setLatitude(userLatDouble);
                addressModel.setLongitude(userLngDouble);

                // Setting up model with parsed data.
                userModel.setId(userId);
                userModel.setFacebookUserId(fbUserId);
                userModel.setRegisteredAt(registeredAt);
                userModel.setUserProducts(productModels);
                userModel.setAddressModel(addressModel);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(userModel);

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

    @Override
    public DAOResponse saveNewUserProduct(String facebookUserId, String productId) {

        DAOResponse daoResponse = new DAOResponse();

        final HashMap<String, String> functionParameters = new HashMap<>();

        functionParameters.put("facebookUserId" , facebookUserId);
        functionParameters.put("productId"      , productId);

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performPostRequest(GeneralValues.BASE_URL + "users/products", functionParameters, false);

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

                // Parsing User Data.
                UserModel userModel = new UserModel();

                JSONObject userJSONObject   = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String userId               = jsonParseDAO.getStringValue(userJSONObject, "_id");
                String fbUserId             = jsonParseDAO.getStringValue(userJSONObject, "facebookUserId");
                String registeredAt         = jsonParseDAO.getStringValue(userJSONObject, "registeredAt");

                // Parsing User Products.
                ArrayList<ProductModel> productModels = new ArrayList<>();
                JSONArray userProductsArray = jsonParseDAO.getArrayFromObject(userJSONObject, "products");
                for(int i = 0; i < userProductsArray.length(); i++){

                    ProductModel productModel   = new ProductModel();
                    JSONObject productAsJSON    = (JSONObject)userProductsArray.get(i);

                    productModel.setName            (jsonParseDAO.getStringValue(productAsJSON, "name"));
                    productModel.setPhotoUrl        (jsonParseDAO.getStringValue(productAsJSON, "photoUrl"));
                    productModel.setPrice           (jsonParseDAO.getStringValue(productAsJSON, "price"));
                    productModel.setQuantity        (jsonParseDAO.getStringValue(productAsJSON, "quantity"));
                    productModel.setBarcodeNumber   (jsonParseDAO.getStringValue(productAsJSON, "barcodeNumber"));
                    productModel.setProductId       (jsonParseDAO.getStringValue(productAsJSON, "_id"));

                    productModels.add(productModel);

                }

                AddressModel addressModel           = new AddressModel();
                JSONObject userAddressJSONObject    = jsonParseDAO.getJsonObjectFromObject(userJSONObject, "address");

                addressModel.setAddress(jsonParseDAO.getStringValue(userAddressJSONObject, "address"));
                addressModel.setBuildingNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "buildingNumber"));
                addressModel.setDoorNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "doorNumber"));
                addressModel.setLandmark(jsonParseDAO.getStringValue(userAddressJSONObject, "landmark"));

                String userLatitude = jsonParseDAO.getStringValue(userAddressJSONObject, "latitude");
                double userLatDouble;
                if(userLatitude.equals(""))
                    userLatDouble = 0;
                else
                    userLatDouble = Double.parseDouble(userLatitude);

                String userLongitude = jsonParseDAO.getStringValue(userAddressJSONObject, "longitude");
                double userLngDouble;
                if(userLongitude.equals(""))
                    userLngDouble = 0;
                else
                    userLngDouble = Double.parseDouble(userLongitude);

                addressModel.setLatitude(userLatDouble);
                addressModel.setLongitude(userLngDouble);

                // Setting up model with parsed data.
                userModel.setId(userId);
                userModel.setFacebookUserId(fbUserId);
                userModel.setRegisteredAt(registeredAt);
                userModel.setUserProducts(productModels);
                userModel.setAddressModel(addressModel);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(userModel);

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

    @Override
    public DAOResponse updateUserProductQuantity(String productId, String facebookUserId, String quantity) {

        DAOResponse daoResponse = new DAOResponse();

        final HashMap<String, String> functionParameters = new HashMap<>();

        functionParameters.put("facebookUserId" , facebookUserId);
        functionParameters.put("quantity"       , quantity);

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performPostRequest(GeneralValues.BASE_URL + "users/products/" + productId, functionParameters, false);

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

                // Parsing User Data.
                UserModel userModel = new UserModel();

                JSONObject userJSONObject   = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String userId               = jsonParseDAO.getStringValue(userJSONObject, "_id");
                String fbUserId             = jsonParseDAO.getStringValue(userJSONObject, "facebookUserId");
                String registeredAt         = jsonParseDAO.getStringValue(userJSONObject, "registeredAt");

                // Parsing User Products.
                ArrayList<ProductModel> productModels = new ArrayList<>();
                JSONArray userProductsArray = jsonParseDAO.getArrayFromObject(userJSONObject, "products");
                for(int i = 0; i < userProductsArray.length(); i++){

                    ProductModel productModel   = new ProductModel();
                    JSONObject productAsJSON    = (JSONObject)userProductsArray.get(i);

                    productModel.setName            (jsonParseDAO.getStringValue(productAsJSON, "name"));
                    productModel.setPhotoUrl        (jsonParseDAO.getStringValue(productAsJSON, "photoUrl"));
                    productModel.setPrice           (jsonParseDAO.getStringValue(productAsJSON, "price"));
                    productModel.setQuantity        (jsonParseDAO.getStringValue(productAsJSON, "quantity"));
                    productModel.setBarcodeNumber   (jsonParseDAO.getStringValue(productAsJSON, "barcodeNumber"));
                    productModel.setProductId       (jsonParseDAO.getStringValue(productAsJSON, "_id"));

                    productModels.add(productModel);

                }

                AddressModel addressModel           = new AddressModel();
                JSONObject userAddressJSONObject    = jsonParseDAO.getJsonObjectFromObject(userJSONObject, "address");

                addressModel.setAddress(jsonParseDAO.getStringValue(userAddressJSONObject, "address"));
                addressModel.setBuildingNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "buildingNumber"));
                addressModel.setDoorNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "doorNumber"));
                addressModel.setLandmark(jsonParseDAO.getStringValue(userAddressJSONObject, "landmark"));

                String userLatitude = jsonParseDAO.getStringValue(userAddressJSONObject, "latitude");
                double userLatDouble;
                if(userLatitude.equals(""))
                    userLatDouble = 0;
                else
                    userLatDouble = Double.parseDouble(userLatitude);

                String userLongitude = jsonParseDAO.getStringValue(userAddressJSONObject, "longitude");
                double userLngDouble;
                if(userLongitude.equals(""))
                    userLngDouble = 0;
                else
                    userLngDouble = Double.parseDouble(userLongitude);

                addressModel.setLatitude(userLatDouble);
                addressModel.setLongitude(userLngDouble);

                // Setting up model with parsed data.
                userModel.setId(userId);
                userModel.setFacebookUserId(fbUserId);
                userModel.setRegisteredAt(registeredAt);
                userModel.setUserProducts(productModels);
                userModel.setAddressModel(addressModel);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(userModel);

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

    @Override
    public DAOResponse updateUserAddress(AddressModel addressModel, String facebookUserId) {

        DAOResponse daoResponse = new DAOResponse();

        final HashMap<String, String> functionParameters = new HashMap<>();

        Gson gson = new Gson();

        functionParameters.put("address.address"        , addressModel.getAddress());
        functionParameters.put("address.doorNumber"     , addressModel.getDoorNumber());
        functionParameters.put("address.buildingNumber" , addressModel.getBuildingNumber());
        functionParameters.put("address.landmark"       , addressModel.getLandmark());
        functionParameters.put("address.latitude"       , addressModel.getLatitude() + "");
        functionParameters.put("address.longitude"      , addressModel.getLongitude() + "");
        functionParameters.put("facebookUserId"         , facebookUserId);

        try {

            IServerRequestDAO serverRequestDAO = new ServerRequestDAOImpl();
            ServerResponseModel serverResponse = serverRequestDAO.performPostRequest(GeneralValues.BASE_URL + "users/address", functionParameters, false);

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

                // Parsing User Data.
                UserModel userModel = new UserModel();

                JSONObject userJSONObject   = jsonParseDAO.getJsonObjectFromObject(responseAsJsonObject, "data");
                String userId               = jsonParseDAO.getStringValue(userJSONObject, "_id");
                String fbUserId             = jsonParseDAO.getStringValue(userJSONObject, "facebookUserId");
                String registeredAt         = jsonParseDAO.getStringValue(userJSONObject, "registeredAt");

                // Parsing User Products.
                ArrayList<ProductModel> productModels = new ArrayList<>();
                JSONArray userProductsArray = jsonParseDAO.getArrayFromObject(userJSONObject, "products");
                for(int i = 0; i < userProductsArray.length(); i++){

                    ProductModel productModel   = new ProductModel();
                    JSONObject productAsJSON    = (JSONObject)userProductsArray.get(i);

                    productModel.setName            (jsonParseDAO.getStringValue(productAsJSON, "name"));
                    productModel.setPhotoUrl        (jsonParseDAO.getStringValue(productAsJSON, "photoUrl"));
                    productModel.setPrice           (jsonParseDAO.getStringValue(productAsJSON, "price"));
                    productModel.setQuantity        (jsonParseDAO.getStringValue(productAsJSON, "quantity"));
                    productModel.setBarcodeNumber   (jsonParseDAO.getStringValue(productAsJSON, "barcodeNumber"));
                    productModel.setProductId       (jsonParseDAO.getStringValue(productAsJSON, "_id"));

                    productModels.add(productModel);

                }

                AddressModel userAddressModel       = new AddressModel();
                JSONObject userAddressJSONObject    = jsonParseDAO.getJsonObjectFromObject(userJSONObject, "address");

                userAddressModel.setAddress(jsonParseDAO.getStringValue(userAddressJSONObject, "address"));
                userAddressModel.setBuildingNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "buildingNumber"));
                userAddressModel.setDoorNumber(jsonParseDAO.getStringValue(userAddressJSONObject, "doorNumber"));
                userAddressModel.setLandmark(jsonParseDAO.getStringValue(userAddressJSONObject, "landmark"));

                String userLatitude = jsonParseDAO.getStringValue(userAddressJSONObject, "latitude");
                double userLatDouble;
                if(userLatitude.equals(""))
                    userLatDouble = 0;
                else
                    userLatDouble = Double.parseDouble(userLatitude);

                String userLongitude = jsonParseDAO.getStringValue(userAddressJSONObject, "longitude");
                double userLngDouble;
                if(userLongitude.equals(""))
                    userLngDouble = 0;
                else
                    userLngDouble = Double.parseDouble(userLongitude);

                addressModel.setLatitude(userLatDouble);
                addressModel.setLongitude(userLngDouble);

                // Setting up model with parsed data.
                userModel.setId(userId);
                userModel.setFacebookUserId(fbUserId);
                userModel.setRegisteredAt(registeredAt);
                userModel.setUserProducts(productModels);
                userModel.setAddressModel(userAddressModel);

                // Setting up DAOResponse with error and data.
                daoResponse.setError(error);
                daoResponse.setObject(userModel);

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
