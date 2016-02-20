package com.otuz.dao;

import com.otuz.model.ServerResponseModel;

import java.util.HashMap;

/**
 * Persistence methods for performing server calls.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public interface IServerRequestDAO {

    /**
     * Performing a server call with a "GET" request.
     * @param requestURL URL that will be requested.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return Server response.
     */
    ServerResponseModel performGetRequest    (String requestURL, boolean isAuthorizationRequired);

    /**
     * Performing a server call with a "DELETE" request.
     * @param requestURL URL that will be requested.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return Server response.
     */
    ServerResponseModel performDeleteRequest (String requestURL, boolean isAuthorizationRequired);

    /**
     * Performing a server call with a "POST" request.
     * @param requestURL URL that will be requested.
     * @param postBodyParams Body parameters for this request.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return Server response.
     */
    ServerResponseModel performPostRequest   (String requestURL, HashMap<String, String> postBodyParams, boolean isAuthorizationRequired);

    /**
     * Performing a server call with a "PUT" request.
     * @param requestURL URL that will be requested.
     * @param putBodyParams Body parameters for this request.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return Server response.
     */
    ServerResponseModel performPutRequest    (String requestURL, HashMap<String, String> putBodyParams, boolean isAuthorizationRequired);

}
