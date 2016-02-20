package com.otuz.controller.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.otuz.R;
import com.otuz.constant.GeneralValues;
import com.otuz.controller.BaseApplication;
import com.otuz.dao.IUserDAO;
import com.otuz.dao.UserDAOImpl;
import com.otuz.model.DAOResponse;
import com.otuz.model.UserModel;
import com.otuz.util.APIErrorCodeHandler;
import com.otuz.util.HttpFailStatusCodeHandler;

/**
 * SplashScreen.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class SplashScreenActivity extends AppCompatActivity{

    private static final int PERMISSIONS_FOR_ACCESS_FINE_LOCATION = 99;

    // A Profile class for holding logged in Facebook user's data.
    private Profile userFacebookProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        // Initialize FacebookSdk before setContentView().
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash_screen);
        splash();

    }

    /**
     * Device network status, permission and logged-in status checking logic.
     */
    private void splash(){

        // Check if device has a network connection.
        if(isDeviceOnline()) {
            // A network connection available.
            // Check all permissions on RunTime for Marshmallow compability.
            checkForPermissionsAtRunTime();

            Handler splashScreenHandler = new Handler();
            splashScreenHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Determining if user logged in through Facebook or not.
                    updateWithToken(AccessToken.getCurrentAccessToken());

                }
            }, GeneralValues.SPLASH_TIME_OUT);

        }else{
            // Device has not a network connection. Toast a message and close application.
            Toast.makeText(SplashScreenActivity.this, getResources().getString(R.string.splash_screen_online_error), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashScreenActivity.this.finish();
                }
            }, GeneralValues.SPLASH_TIME_OUT);
        }

    }

    private void checkForPermissionsAtRunTime(){

        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_FOR_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    /**
     * Determine if device has a network connection.
     * @return a boolean for network availability.
     */
    private boolean isDeviceOnline(){

        ConnectivityManager connectivityManager = (ConnectivityManager)SplashScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Return true if and only if there is an active network and this network's current status is "connected".
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    /**
     * Updating UI or doing different operations due to Facebook AccessToken.
     * @param currentAccessToken Currently available Facebook AccessToken.
     */
    private void updateWithToken(final AccessToken currentAccessToken) {

        if (currentAccessToken != null && !currentAccessToken.isExpired()) {

            // User is logged in.
            // Getting Facebook profile data of logged in user.
            userFacebookProfile = Profile.getCurrentProfile();

            Log.d("Current FB Profile : ", userFacebookProfile + "");

            if (userFacebookProfile != null) {

                // User logged in with Facebook before. Get user data from server.

                final Handler serverRequestHandler = new Handler();
                Thread getUserThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        IUserDAO userDAO = new UserDAOImpl();
                        final DAOResponse daoResponse = userDAO.getUser(currentAccessToken.getUserId());

                        // Handling server response.
                        serverRequestHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (daoResponse.getError().getErrorCode() == 0) {
                                    // Success.
                                    UserModel userModel = (UserModel) daoResponse.getObject();

                                    // Store User data locally but temporary.
                                    BaseApplication.setUserModel(userModel);

                                    startActivity(new Intent(SplashScreenActivity.this, ShoppingCartActivity.class));
                                    SplashScreenActivity.this.finish();

                                } else {
                                    // Check if the error code is a Http status code.
                                    HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(SplashScreenActivity.this);
                                    if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                        // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                        APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(SplashScreenActivity.this);
                                        apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode());
                                    }

                                }

                            }
                        });
                    }
                });
                getUserThread.start();

            }

        } else {

            // User is not logged in.
            startActivity(new Intent(SplashScreenActivity.this, WalkthroughActivity.class));
            SplashScreenActivity.this.finish();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FOR_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    SplashScreenActivity.this.finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
