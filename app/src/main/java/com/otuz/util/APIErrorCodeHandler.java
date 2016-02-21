package com.otuz.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

    public void handleErrorCode(int errorCode, String _errorMessage){

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
                errorMessage = _errorMessage;
                break;

        }

        Log.e("API Error", "Code : " + errorCode + " - Message : " + errorMessage);
        showSnackBar(((AppCompatActivity)context), errorMessage);

    }

    private void showSnackBar(Activity _activity, String _text){
        Snackbar snackbar = Snackbar.make(_activity.findViewById(android.R.id.content), _text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
