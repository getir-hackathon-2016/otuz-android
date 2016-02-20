package com.otuz.util;

import android.content.Context;
import android.widget.Toast;

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

        Toast.makeText(context, "hata = " + errorCode, Toast.LENGTH_SHORT).show();

    }

}
