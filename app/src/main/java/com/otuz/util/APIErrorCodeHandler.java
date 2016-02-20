package com.otuz.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.otuz.R;

/**
 * Handle Burhan's error codes.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class APIErrorCodeHandler {

    private Context context;

    public APIErrorCodeHandler(Context context){
        this.context = context;
    }

    public void handleErrorCode(int errorCode){

        String errorMessage;

        switch(errorCode) {

            case 601:
                errorMessage = context.getResources().getString(R.string.api_error_1);
                break;
            case 602:
                errorMessage = context.getResources().getString(R.string.api_error_2);
                break;
            case 603:
                errorMessage = context.getResources().getString(R.string.api_error_3);
                break;
            default  :
                errorMessage = "";
                break;

        }

        Log.e("API Error", "Code : " + errorCode + " - Message : " + errorMessage);
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();

    }

}
