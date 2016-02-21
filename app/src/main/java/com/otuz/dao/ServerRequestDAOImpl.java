package com.otuz.dao;

import android.util.Log;

import com.otuz.constant.GeneralValues;
import com.otuz.model.ServerResponseModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Makes server calls.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ServerRequestDAOImpl implements IServerRequestDAO{

    /**
     * Performing a "GET" request.
     * @param requestURL URL that will be requested.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return Returns a ServerResponseModel
     */
    @Override
    public ServerResponseModel performGetRequest(String requestURL, boolean isAuthorizationRequired) {

        URL url;
        HttpURLConnection conn = null;
        ServerResponseModel serverResponse = new ServerResponseModel();

        try {

            // Setting up and opening a new connection to server.
            url     = new URL(requestURL);
            conn    = (HttpURLConnection) url.openConnection();

            // Adding Authorization property with access token if authorization required for this request.
            if (isAuthorizationRequired)
                conn.setRequestProperty("Authorization", "Bearer " + "access_token");

            // Setting server time-outs and request method to opened connection.
            conn.setReadTimeout     (GeneralValues.SERVER_READ_TIME_OUT);
            conn.setConnectTimeout  (GeneralValues.SERVER_CONNECT_TIME_OUT);
            conn.setRequestMethod   (GeneralValues.SERVER_GET_REQUEST);

            // Set below property as "true" because we should be able to get a response from server.
            conn.setDoInput(true);

            // Getting the server response as a String.
            serverResponse = getResponseFromConnection(conn);

            Log.d(GeneralValues.APP_TAG, serverResponse.toString());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                // Server connection no longer needed.
                conn.disconnect();
            }
        }

        return serverResponse;

    }

    /**
     * Performing a "DELETE" request.
     * @param requestURL URL that will be requested.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return
     */
    @Override
    public ServerResponseModel performDeleteRequest(String requestURL, boolean isAuthorizationRequired) {

        URL url;
        HttpURLConnection conn = null;
        ServerResponseModel serverResponse = new ServerResponseModel();

        try {

            // Setting up and opening a new connection to server.
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();

            // Adding Authorization property with access token if authorization required for this request.
            if (isAuthorizationRequired)
                conn.setRequestProperty("Authorization", "Bearer " + "access_token");

            // Setting server time-outs and request method to opened connection.
            conn.setReadTimeout(GeneralValues.SERVER_READ_TIME_OUT);
            conn.setConnectTimeout(GeneralValues.SERVER_CONNECT_TIME_OUT);
            conn.setRequestMethod(GeneralValues.SERVER_DELETE_REQUEST);

            // Set below property as "true" because we should be able to get a response from server.
            conn.setDoInput(true);

            // Getting the server response as a String.
            serverResponse = getResponseFromConnection(conn);

            Log.d(GeneralValues.APP_TAG, serverResponse.toString());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                // Server connection no longer needed.
                conn.disconnect();
            }
        }
        return serverResponse;

    }

    /**
     * Performing a "POST" request.
     * @param requestURL URL that will be requested.
     * @param postBodyParams Body parameters for this request.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return
     */
    @Override
    public ServerResponseModel performPostRequest(String requestURL, HashMap<String, String> postBodyParams, boolean isAuthorizationRequired) {

        URL url;
        HttpURLConnection conn = null;
        ServerResponseModel serverResponse = new ServerResponseModel();

        try {

            // Setting up and opening a new connection to server.
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();

            // Adding Authorization property with access token if authorization required for this request.
            if (isAuthorizationRequired)
                conn.setRequestProperty("Authorization", "Bearer " + "access_token");

            // Setting server time-outs and request method to opened connection.
            conn.setRequestProperty ("Content-Type", "application/json; charset=utf-8");
            conn.setReadTimeout     (GeneralValues.SERVER_READ_TIME_OUT);
            conn.setConnectTimeout  (GeneralValues.SERVER_CONNECT_TIME_OUT);
            conn.setRequestMethod   (GeneralValues.SERVER_POST_REQUEST);

            // Set below property as "true" because we should be able to get a response from server.
            conn.setDoInput (true);

            // Set below property as "true" because we should be able to send a body request to server.
            conn.setDoOutput(true);

            // Getting an output stream of connection and writing the body parameters to it.
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getParametersString(postBodyParams));
            writer.flush();
            writer.close();
            os.close();

            // Getting the server response as a String.
            serverResponse = getResponseFromConnection(conn);

            Log.d(GeneralValues.APP_TAG, serverResponse.toString());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                // Server connection no longer needed.
                conn.disconnect();
            }
        }
        return serverResponse;

    }

    /**
     * Performing a "PUT" request.
     * @param requestURL URL that will be requested.
     * @param putBodyParams Body parameters for this request.
     * @param isAuthorizationRequired Is authorization required for this request or not.
     * @return
     */
    @Override
    public ServerResponseModel performPutRequest(String requestURL, HashMap<String, String> putBodyParams, boolean isAuthorizationRequired) {

        URL url;
        HttpURLConnection conn = null;
        ServerResponseModel serverResponse = new ServerResponseModel();

        try {

            // Setting up and opening a new connection to server.
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();

            // Adding Authorization property with access token if authorization required for this request.
            if (isAuthorizationRequired)
                conn.setRequestProperty("Authorization", "Bearer " + "access_token");

            // Setting server time-outs and request method to opened connection.
            conn.setReadTimeout     (GeneralValues.SERVER_READ_TIME_OUT);
            conn.setConnectTimeout  (GeneralValues.SERVER_CONNECT_TIME_OUT);
            conn.setRequestMethod   (GeneralValues.SERVER_PUT_REQUEST);

            // Set below property as "true" because we should be able to get a response from server.
            conn.setDoInput (true);

            // Set below property as "true" because we should be able to send a body request to server.
            conn.setDoOutput(true);

            // Getting an output stream of connection and writing the body parameters to it.
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getParametersString(putBodyParams));
            writer.flush();
            writer.close();
            os.close();

            // Getting the server response as a String.
            serverResponse = getResponseFromConnection(conn);

            Log.d(GeneralValues.APP_TAG, serverResponse.toString());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                // Server connection no longer needed.
                conn.disconnect();
            }
        }
        return serverResponse;

    }

    /**
     * Getting server response as a String.
     * @param conn Current (alive) server connection.
     * @return Server response as String.
     */
    private ServerResponseModel getResponseFromConnection(HttpURLConnection conn) {

        String response = "";
        ServerResponseModel serverResponse = new ServerResponseModel();

        try {

            InputStreamReader inStreamReader;

            // Getting response code for current request.
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Response code == 200 so get input stream from connection to InputStreamReader.
                inStreamReader = new InputStreamReader(conn.getInputStream());
                serverResponse.setSuccess(true);
            } else {
                // Response code != 200 so get error stream from connection to InputStreamReader.
                inStreamReader = new InputStreamReader(conn.getErrorStream());
                serverResponse.setSuccess(false);
            }

            String line;
            BufferedReader br = new BufferedReader(inStreamReader);

            // Read the getted stream line by line.
            while ((line = br.readLine()) != null) {
                response += line;
            }

            serverResponse.setCode(responseCode);
            serverResponse.setResponse(response);

        } catch (IOException ioExcp) {
            ioExcp.printStackTrace();
        }

        // Return server response for current request.
        return serverResponse;

    }

    /**
     * Getting formatted parameters as String.
     * @param params End-point parameters as HashMap.
     * @return Formatted parameters as String
     * @throws UnsupportedEncodingException
     */
    private String getParametersString(HashMap<String, String> params) throws UnsupportedEncodingException {

        String result;
        boolean first = true;
        /*for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }*/
        JSONObject paramsAsJSON = new JSONObject(params);
        result = paramsAsJSON.toString();
        Log.e("result.toString()", result);
        return result;

    }

}
