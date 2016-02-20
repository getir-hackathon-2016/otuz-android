package com.otuz.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.otuz.R;

/**
 * Logs the Http status message with status code except "success" (2xx) codes.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class HttpFailStatusCodeHandler {

    private Context context;

    public HttpFailStatusCodeHandler(Context context){
        this.context = context;
    }

    public boolean handleCode(int statusCode){

        String statusMessage;
        boolean codeIsHttpStatusCode = false;

        switch(statusCode){

            // Informational.
            case 100 :
                statusMessage = "Continue";
                codeIsHttpStatusCode = true;
                break;
            case 101 :
                statusMessage = "Switching Protocols";
                codeIsHttpStatusCode = true;
                break;
            case 102 :
                statusMessage = "Processing ";
                codeIsHttpStatusCode = true;
                break;

            // Redirection.
            case 300 :
                statusMessage = "Multiple Choices";
                codeIsHttpStatusCode = true;
                break;
            case 301 :
                statusMessage = "Moved Permanently";
                codeIsHttpStatusCode = true;
                break;
            case 302 :
                statusMessage = "Found";
                codeIsHttpStatusCode = true;
                break;
            case 303 :
                statusMessage = "See Other";
                codeIsHttpStatusCode = true;
                break;
            case 304 :
                statusMessage = "Not Modified";
                codeIsHttpStatusCode = true;
                break;
            case 305 :
                statusMessage = "Use Proxy";
                codeIsHttpStatusCode = true;
                break;
            case 306 :
                statusMessage = "Switch Proxy";
                codeIsHttpStatusCode = true;
                break;
            case 307 :
                statusMessage = "Temporary Redirect";
                codeIsHttpStatusCode = true;
                break;
            case 308 :
                statusMessage = "Permanent Redirect";
                codeIsHttpStatusCode = true;
                break;

            // Client Errors.
            case 400 :
                statusMessage = "Bad Request";
                codeIsHttpStatusCode = true;
                break;
            case 401 :
                statusMessage = "Unauthorized";
                codeIsHttpStatusCode = true;
                break;
            case 402 :
                statusMessage = "Payment Required";
                codeIsHttpStatusCode = true;
                break;
            case 403 :
                statusMessage = "Forbidden";
                codeIsHttpStatusCode = true;
                break;
            case 404 :
                statusMessage = "Not Found";
                codeIsHttpStatusCode = true;
                break;
            case 405 :
                statusMessage = "Method Not Allowed";
                codeIsHttpStatusCode = true;
                break;
            case 406 :
                statusMessage = "Not Acceptable";
                codeIsHttpStatusCode = true;
                break;
            case 407 :
                statusMessage = "Login Required On Proxy Server";
                codeIsHttpStatusCode = true;
                break;
            case 408 :
                statusMessage = "Request Timeout";
                codeIsHttpStatusCode = true;
                break;
            case 409 :
                statusMessage = "Conflict";
                codeIsHttpStatusCode = true;
                break;
            case 410 :
                statusMessage = "Gone";
                codeIsHttpStatusCode = true;
                break;
            case 411 :
                statusMessage = "Length Required";
                codeIsHttpStatusCode = true;
                break;
            case 412 :
                statusMessage = "Precondition Failed";
                codeIsHttpStatusCode = true;
                break;
            case 413 :
                statusMessage = "Request Entity Too Large";
                codeIsHttpStatusCode = true;
                break;
            case 414 :
                statusMessage = "Request-URI Too Long";
                codeIsHttpStatusCode = true;
                break;
            case 415 :
                statusMessage = "Unsupported Media Type";
                codeIsHttpStatusCode = true;
                break;
            case 416 :
                statusMessage = "Requested Range Unsatifiable";
                codeIsHttpStatusCode = true;
                break;
            case 417 :
                statusMessage = "Expectation Failed";
                codeIsHttpStatusCode = true;
                break;
            case 418 :
                statusMessage = "I'm a teapot";
                codeIsHttpStatusCode = true;
                break;
            case 421 :
                statusMessage = "Misdirected Request";
                codeIsHttpStatusCode = true;
                break;
            case 422 :
                statusMessage = "Unprocessable Entity";
                codeIsHttpStatusCode = true;
                break;
            case 423 :
                statusMessage = "Locked";
                codeIsHttpStatusCode = true;
                break;
            case 424 :
                statusMessage = "Method Failure";
                codeIsHttpStatusCode = true;
                break;
            case 426 :
                statusMessage = "Upgrade Required";
                codeIsHttpStatusCode = true;
                break;
            case 428 :
                statusMessage = "Precondition Required";
                codeIsHttpStatusCode = true;
                break;
            case 429 :
                statusMessage = "Too Many Requests";
                codeIsHttpStatusCode = true;
                break;
            case 431 :
                statusMessage = "Request Header Fields Too Large";
                codeIsHttpStatusCode = true;
                break;
            case 451 :
                statusMessage = "Unavailable For Legal Reasons";
                codeIsHttpStatusCode = true;
                break;

            // Server Errors.
            case 500 :
                statusMessage = "Internal Server Error";
                codeIsHttpStatusCode = true;
                break;
            case 501 :
                statusMessage = "Not Implemented";
                codeIsHttpStatusCode = true;
                break;
            case 502 :
                statusMessage = "Bad Gateway";
                codeIsHttpStatusCode = true;
                break;
            case 503 :
                statusMessage = "Service Unavailable";
                codeIsHttpStatusCode = true;
                break;
            case 504 :
                statusMessage = "Gateway Timeout";
                codeIsHttpStatusCode = true;
                break;
            case 505 :
                statusMessage = "HTTP Version Not Supported";
                codeIsHttpStatusCode = true;
                break;
            case 507 :
                statusMessage = "Insufficient storage";
                codeIsHttpStatusCode = true;
                break;
            case 508 :
                statusMessage = "Loop Detected";
                codeIsHttpStatusCode = true;
                break;
            case 510 :
                statusMessage = "Not Extended";
                codeIsHttpStatusCode = true;
                break;
            case 511 :
                statusMessage = "Network Authentication Required";
                codeIsHttpStatusCode = true;
                break;

            // This code is custom.
            case 1000:
                statusMessage = "Server time-out";
                codeIsHttpStatusCode = true;
                break;
            default  :
                statusMessage = "";
                codeIsHttpStatusCode = false;
                break;

        }

        if(codeIsHttpStatusCode) {
            Log.e("Http status", "Code : " + statusCode + " - Message : " + statusMessage);
            Toast.makeText(context, context.getResources().getString(R.string.warning_1), Toast.LENGTH_SHORT).show();
        }

        return codeIsHttpStatusCode;

    }

}
