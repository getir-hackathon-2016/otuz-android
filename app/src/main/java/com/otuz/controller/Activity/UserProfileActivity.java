package com.otuz.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.otuz.R;
import com.otuz.controller.BaseApplication;
import com.otuz.dao.IMapDAO;
import com.otuz.dao.IUserDAO;
import com.otuz.dao.MapDAOImpl;
import com.otuz.dao.UserDAOImpl;
import com.otuz.model.AddressModel;
import com.otuz.model.DAOResponse;
import com.otuz.model.MapLocationModel;
import com.otuz.model.UserModel;
import com.otuz.util.APIErrorCodeHandler;
import com.otuz.util.HttpFailStatusCodeHandler;

/**
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public class UserProfileActivity extends Activity{

    private static final float MAP_ZOOM = 16f;

    private GoogleMap googleMap;
    private MapFragment mapFragment;

    private Button updateUserAddressButton;
    private TextView fillAddressFromMapButton;

    private EditText addressEditText, buildingNoEditText, doorNoEditText, landmarkEditText;

    private AddressModel addressModel = new AddressModel();

    private View.OnClickListener updateUserAddressOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateUserAddress();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mapFragment                 = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map_fragment);
        updateUserAddressButton     = (Button) findViewById(R.id.update_user_address_button);
        fillAddressFromMapButton    = (TextView) findViewById(R.id.fill_address_from_map_button);
        addressEditText             = (EditText) findViewById(R.id.address_edit_text);
        buildingNoEditText          = (EditText) findViewById(R.id.building_no_edit_text);
        doorNoEditText              = (EditText) findViewById(R.id.door_no_edit_text);
        landmarkEditText            = (EditText) findViewById(R.id.landmark_edit_text);

        double userLat = BaseApplication.getUserModel().getAddressModel().getLatitude();
        double userLng = BaseApplication.getUserModel().getAddressModel().getLongitude();

        if(userLat != 0 && userLng != 0){
            setUpMap(userLat, userLng);
        }else {
            setUpMap(41.032200, 28.982670);
        }

        updateUserAddressButton.setOnClickListener(updateUserAddressOnClickListener);

    }

    /**
     * Setting up Google Maps.
     * @param _latitude Opening latitude.
     * @param _longitude Opening longitude.
     */
    private void setUpMap(double _latitude, double _longitude){

        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);

        LatLng mapPosition = new LatLng(_latitude, _longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPosition, MAP_ZOOM));

        fillAddressFromMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final double markerLatitude = googleMap.getCameraPosition().target.latitude;
                final double markerLongitude = googleMap.getCameraPosition().target.longitude;

                final Handler formattedAddressHandler = new Handler();
                Thread formattedAddressThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        IMapDAO mapDAO = new MapDAOImpl();
                        final DAOResponse daoResponse = mapDAO.getFormattedAddressFromGoogleMaps(markerLatitude, markerLongitude);

                        // Handling server response.
                        formattedAddressHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (daoResponse.getError().getErrorCode() == 0) {
                                    // Success.
                                    MapLocationModel mapLocationModel = (MapLocationModel) daoResponse.getObject();
                                    addressEditText.setText(mapLocationModel.getFormattedAddress());
                                    addressModel.setLatitude(markerLatitude);
                                    addressModel.setLongitude(markerLongitude);

                                } else {
                                    // Check if the error code is a Http status code.
                                    HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(UserProfileActivity.this);
                                    if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                        // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                        APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(UserProfileActivity.this);
                                        apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                                    }

                                }

                            }
                        });
                    }
                });
                formattedAddressThread.start();

            }

        });

    }

    /**
     * Update user address with form data.
     */
    private void updateUserAddress(){

        String userAddress      = addressEditText.getText().toString();
        String userBuildingNo   = buildingNoEditText.getText().toString();
        String userDoorNo       = doorNoEditText.getText().toString();
        String userLandmark     = landmarkEditText.getText().toString();

        // Must not empty fields check.
        if(userAddress.equals("") || userBuildingNo.equals("") || userDoorNo.equals("")){
            Snackbar snackbar = Snackbar.make(UserProfileActivity.this.findViewById(android.R.id.content), getResources().getString(R.string.warning_4), Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(UserProfileActivity.this, R.color.white));
            snackbar.show();
        }else{

            addressModel.setAddress(userAddress);
            addressModel.setBuildingNumber(userBuildingNo);
            addressModel.setDoorNumber(userDoorNo);
            addressModel.setLandmark(userLandmark);

            final Handler updateUserAddressHandler = new Handler();
            Thread updateUserAddressThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    IUserDAO userDAO = new UserDAOImpl();
                    final DAOResponse daoResponse = userDAO.updateUserAddress(addressModel, BaseApplication.getUserModel().getFacebookUserId());

                    // Handling server response.
                    updateUserAddressHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (daoResponse.getError().getErrorCode() == 0) {
                                // Success.
                                UserModel userModel = (UserModel) daoResponse.getObject();

                                // Store User data locally but temporary.
                                BaseApplication.setUserModel(userModel);

                            } else {
                                // Check if the error code is a Http status code.
                                HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(UserProfileActivity.this);
                                if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                    // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                    APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(UserProfileActivity.this);
                                    apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                                }

                            }

                        }
                    });
                }
            });
            updateUserAddressThread.start();

        }

    }

}
