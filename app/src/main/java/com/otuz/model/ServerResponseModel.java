package com.otuz.model;

/**
 * A POJO for holding server response string, http response code and a boolean for HTTP connection success status.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ServerResponseModel {

    // Returned string from server.
    private String response;

    // Default code is 1000. If there isn't any problem with server connection this value will be overrided.
    // Checked this in controller. If it is 1000 then you will know that a server time-out occured.
    // (UnknownHostException is also triggers this situation but you shouldn't type the URL wrong already.)
    private int code = 1000;

    // true if there isn't any problem on server connection, false otherwise.
    private boolean success = false;

    /**
     * Getting whole response as string for logging purposes.
     * @return Response as string.
     */
    public String toString(){
        return "Response Code : " + this.code + " - Response String : " + this.response;
    }

    // Getters and Setters.
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
