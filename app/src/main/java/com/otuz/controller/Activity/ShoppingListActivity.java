package com.otuz.controller.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.otuz.R;
import com.otuz.adapter.ProductRecyclerViewAdapter;
import com.otuz.controller.BaseApplication;
import com.otuz.dao.IOrderDAO;
import com.otuz.dao.IProductDAO;
import com.otuz.dao.ISharedPreferencesDAO;
import com.otuz.dao.IUserDAO;
import com.otuz.dao.OrderDAOImpl;
import com.otuz.dao.ProductDAOImpl;
import com.otuz.dao.SharedPreferencesDAOImpl;
import com.otuz.dao.UserDAOImpl;
import com.otuz.listener.DatePickedListener;
import com.otuz.listener.ProductConfirmListener;
import com.otuz.listener.UserProductQuantityChangeListener;
import com.otuz.model.DAOResponse;
import com.otuz.model.ProductModel;
import com.otuz.model.UserModel;
import com.otuz.util.APIErrorCodeHandler;
import com.otuz.util.DatePickerDialogFragment;
import com.otuz.util.HttpFailStatusCodeHandler;
import com.otuz.util.ProductInfoDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * An Activity for user's shopping list data.
 * Displays user's shopping cart.
 */
public class ShoppingListActivity extends AppCompatActivity {

    private static final int BARCODE_SCAN_REQUEST_CODE = 98;

    private ProductModel productModel;

    private TextView chooseDateButton, emptyListMessage, confirmShoppingListButton;

    private ProductRecyclerViewAdapter productRecyclerViewAdapter;

    private ArrayList<ProductModel> userProductModels = new ArrayList<>();

    private ISharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAOImpl();

    private ProductConfirmListener productConfirmListener = new ProductConfirmListener() {
        @Override
        public void OnProductConfirmed() {
            confirmProduct();
        }
    };

    private String deliveryDate;

    // Pass this listener to DatePickerDialog. So you will be able to perform some logics when date picked.
    private DatePickedListener datePickedListener = new DatePickedListener() {
        @Override
        public void onDatePicked(String deliveryDate) {
            sharedPreferencesDAO.setValue("delivery_date", deliveryDate);
            chooseDateButton.setText(sharedPreferencesDAO.getValue("delivery_date") + " - " + sharedPreferencesDAO.getValue("delivery_time"));
            showTimePickerDialog();
        }
    };

    // Pass this to RecylerViewAdapter.
    // In the adapter, when any row's increase/decrease quantity button gets clicked you can be able to notify the adapter explicitly.
    // Should be this way cause whole product list is storing at the BaseApplication class.
    private UserProductQuantityChangeListener userProductQuantityChangeListener = new UserProductQuantityChangeListener() {
        @Override
        public void onChangeOccurs() {
            userProductModels.clear();
            userProductModels.addAll(BaseApplication.getUserModel().getUserProducts());
            productRecyclerViewAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        final Toolbar toolbar       = (Toolbar) findViewById(R.id.toolbar);
        final ImageView mapButton   = (ImageView) findViewById(R.id.map_button);
        RecyclerView recyclerView   = (RecyclerView) findViewById(R.id.recycler_view);
        chooseDateButton            = (TextView) findViewById(R.id.choose_date_button);
        emptyListMessage            = (TextView) findViewById(R.id.empty_list_message);
        confirmShoppingListButton   = (TextView) findViewById(R.id.confirm_shopping_list);

        toolbar.setTitle(getResources().getString(R.string.user_cart_title));
        setSupportActionBar(toolbar);

        userProductModels.addAll(BaseApplication.getUserModel().getUserProducts());

        // If list has at least 1 product, hide the empty list message.
        if(userProductModels.size() > 0)
            emptyListMessage.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(false);
        productRecyclerViewAdapter = new ProductRecyclerViewAdapter(userProductModels, R.layout.row_cart_product, this, userProductQuantityChangeListener);
        recyclerView.setAdapter(productRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Give it's functionality to floating action button. (Open BarcodeScanActivity for adding a new product)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShoppingListActivity.this, BarcodeScanActivity.class), BARCODE_SCAN_REQUEST_CODE);
            }
        });

        // Open DatePickerDialog.
        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment picker = new DatePickerDialogFragment();
                picker.setDatePickedListener(datePickedListener);
                picker.show(getFragmentManager(), "date_picker");
            }
        });

        // Open user profile.
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingListActivity.this, UserProfileActivity.class));
            }
        });

        // Format locally stored date so server can recognize it.
        confirmShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Formatting locally stored date.
                    SimpleDateFormat formatWithDayName  = new SimpleDateFormat("MMMM, dd EEEE", new Locale("tr"));
                    Date date       = formatWithDayName.parse(sharedPreferencesDAO.getValue("delivery_date"));
                    deliveryDate    = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(date);
                    deliveryDate    = deliveryDate + "-2016 " + sharedPreferencesDAO.getValue("delivery_time");

                    // Make server request to confirm shopping list.
                    final Handler serverRequestHandler = new Handler();
                    Thread confirmShoppingListThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            IOrderDAO orderDAO = new OrderDAOImpl();
                            final DAOResponse daoResponse = orderDAO.confirmShoppingList(
                                    BaseApplication.getUserModel().getFacebookUserId(),
                                    deliveryDate
                            );

                            // Handling server response.
                            serverRequestHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (daoResponse.getError().getErrorCode() == 0) {

                                        Snackbar.make(ShoppingListActivity.this.findViewById(android.R.id.content), getResources().getString(R.string.success_2), Snackbar.LENGTH_LONG).show();

                                    } else {
                                        // Check if the error code is a Http status code.
                                        HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(ShoppingListActivity.this);
                                        if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                            // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                            APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(ShoppingListActivity.this);
                                            apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                                        }

                                    }

                                }
                            });
                        }
                    });
                    confirmShoppingListThread.start();

                }catch(Exception e){e.printStackTrace();}
            }
        });

        // If user does not choose a delivery date and time, he/she must do it when shopping list open.
        if(sharedPreferencesDAO.getValue("delivery_date").equals("")){
            chooseDateButton.callOnClick();
        }else if(sharedPreferencesDAO.getValue("delivery_time").equals("")){
            showTimePickerDialog();
        }else{
            chooseDateButton.setText(sharedPreferencesDAO.getValue("delivery_date") + " - " + sharedPreferencesDAO.getValue("delivery_time"));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get returned barcode number.
        if (requestCode == BARCODE_SCAN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String barcodeNo = data.getStringExtra("barcode_number");
                Log.d("barcode no ", " ==> " + barcodeNo);
                getProductData(barcodeNo);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ShoppingListActivity.this, getResources().getString(R.string.warning_2), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShoppingListActivity.this, getResources().getString(R.string.warning_3), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Get product data with barcode number.
     * @param barcodeNo
     */
    private void getProductData(final String barcodeNo){

        final Handler serverRequestHandler = new Handler();
        Thread getProductThread = new Thread(new Runnable() {
            @Override
            public void run() {

                IProductDAO productDAO = new ProductDAOImpl();
                final DAOResponse daoResponse = productDAO.getProductViaBarcodeNumber(barcodeNo);

                // Handling server response.
                serverRequestHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (daoResponse.getError().getErrorCode() == 0) {
                            // Success.
                            productModel = (ProductModel) daoResponse.getObject();
                            showProductInfoDialog(productModel.getPhotoUrl());

                        } else {
                            // Check if the error code is a Http status code.
                            HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(ShoppingListActivity.this);
                            if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(ShoppingListActivity.this);
                                apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                            }

                        }

                    }
                });
            }
        });
        getProductThread.start();

    }

    /**
     * Show product info in a Dialog box.
     * @param photoUrl Product's photo url.
     */
    private void showProductInfoDialog(String photoUrl){
        ProductInfoDialog productInfoDialog = new ProductInfoDialog();
        Bundle argument = new Bundle(1);
        argument.putString("photo_url", photoUrl);
        productInfoDialog.setArguments(argument);
        productInfoDialog.setProductConfirmListener(productConfirmListener);
        productInfoDialog.show(ShoppingListActivity.this.getFragmentManager(), "product_info_dialog");
    }

    /**
     * User confirms the displayed product is the one him/her scanned.
     * When confirming occurs, product will be added to user's shopping list.
     */
    private void confirmProduct(){

        final Handler serverRequestHandler = new Handler();
        Thread saveANewUserProductThread = new Thread(new Runnable() {
            @Override
            public void run() {

                IUserDAO userDAO = new UserDAOImpl();
                final DAOResponse daoResponse = userDAO.saveNewUserProduct(
                        BaseApplication.getUserModel().getFacebookUserId(),
                        productModel.getProductId()
                );

                // Handling server response.
                serverRequestHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (daoResponse.getError().getErrorCode() == 0) {
                            // Success.
                            UserModel userModel = (UserModel) daoResponse.getObject();

                            // Store User data locally but temporary.
                            BaseApplication.setUserModel(userModel);

                            userProductModels.clear();
                            userProductModels.addAll(BaseApplication.getUserModel().getUserProducts());

                            productRecyclerViewAdapter.notifyDataSetChanged();

                            Snackbar.make(ShoppingListActivity.this.findViewById(android.R.id.content), getResources().getString(R.string.success_1), Snackbar.LENGTH_LONG).show();

                        } else {
                            // Check if the error code is a Http status code.
                            HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(ShoppingListActivity.this);
                            if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(ShoppingListActivity.this);
                                apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode(),daoResponse.getError().getErrorMessage());
                            }

                        }

                    }
                });
            }
        });
        saveANewUserProductThread.start();

    }

    /**
     * Show a TimePicker for delivery time.
     */
    private void showTimePickerDialog(){

        Calendar currentTime = Calendar.getInstance();
        int hour    = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute  = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(ShoppingListActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String selectedHourInString;
                String selectedMinuteInString;

                if(selectedHour/10 == 0)
                    selectedHourInString = "0" + selectedHour;
                else
                    selectedHourInString = "" + selectedHour;

                if(selectedMinute/10 == 0)
                    selectedMinuteInString = "0" + selectedMinute;
                else
                    selectedMinuteInString = "" + selectedMinute;

                sharedPreferencesDAO.setValue("delivery_time", selectedHourInString + ":" + selectedMinuteInString);
                chooseDateButton.setText(sharedPreferencesDAO.getValue("delivery_date") + " - " + sharedPreferencesDAO.getValue("delivery_time"));

            }
        }, hour, minute, true);
        timePicker.setTitle(getResources().getString(R.string.warning_5));
        timePicker.show();

    }

}
