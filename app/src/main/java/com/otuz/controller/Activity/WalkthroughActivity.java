package com.otuz.controller.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.otuz.R;
import com.otuz.controller.BaseApplication;
import com.otuz.controller.fragment.WalkthroughContainerFragment;
import com.otuz.dao.IUserDAO;
import com.otuz.dao.UserDAOImpl;
import com.otuz.model.DAOResponse;
import com.otuz.model.UserModel;
import com.otuz.model.WalkthroughModel;
import com.otuz.util.APIErrorCodeHandler;
import com.otuz.util.HttpFailStatusCodeHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * An Activity for helping not logged-in users.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class WalkthroughActivity extends AppCompatActivity {

    // A CallbackManager for setting it up to Facebook's LoginButton.
    private CallbackManager callbackManager;
    // A Profile class for holding logged in Facebook user's data.
    private Profile userFacebookProfile;
    // Facebook login button.
    private LoginButton facebookSDKLoginButton;

    // For holding walkthrough pages.
    private ViewPager viewPager;
    // Number of walkthrough pages.
    private int numberOfViewPagerChildren;
    // An ArrayList which holds walkthrough pages (fragment) for giving them to ViewPager's FragmentStatePagerAdapter.
    private ArrayList<Fragment> walkthroughFragmentList = new ArrayList<>();
    // An ArrayList which holds oval backgrounded View's. These are page number indicator balls.
    private ArrayList<View> ballList;

    private TextView nextButton;
    private LinearLayout facebookLogin;

    private View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewPager.getCurrentItem() < numberOfViewPagerChildren - 1)
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            if(viewPager.getCurrentItem() == numberOfViewPagerChildren - 1){
                nextButton.setVisibility(View.GONE);
                facebookLogin.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener facebookLoginButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Calling Facebook SDK login button's onClick method when our custom button clicked.
            facebookSDKLoginButton.callOnClick();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialize FacebookSdk before setContentView().
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_walkthrough);

        viewPager       = (ViewPager) findViewById(R.id.walkthrough_view_pager);
        nextButton      = (TextView) findViewById(R.id.next_button);
        facebookLogin   = (LinearLayout) findViewById(R.id.facebook_login);

        facebookSDKLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);

        // Setting up CallbackManager and AccessTokenTracker for Facebook Login.
        callbackManager = CallbackManager.Factory.create();

        // Setting up Facebook login permissions. (Just request for public profile data in this case)
        facebookSDKLoginButton.setReadPermissions("public_profile");

        // Callback registration for Facebook login.
        facebookSDKLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            /**
             * If logging in operation successfully completed.
             * @param loginResult It has the access token and permissions.
             */
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("FB loginResult ", "Granted Permissions                      = " + loginResult.getRecentlyGrantedPermissions() + "");
                Log.d("FB loginResult ", "Denied Permissions                       = " + loginResult.getRecentlyDeniedPermissions() + "");
                Log.d("FB loginResult ", "Access Token                             = " + loginResult.getAccessToken() + "");
                Log.d("FB loginResult ", "Access Token getApplicationId()          = " + loginResult.getAccessToken().getApplicationId() + "");
                Log.d("FB loginResult ", "Access Token getToken()                  = " + loginResult.getAccessToken().getToken() + "");
                Log.d("FB loginResult ", "Access Token getUserId()                 = " + loginResult.getAccessToken().getUserId() + "");
                Log.d("FB loginResult ", "Access Token getDeclinedPermissions()    = " + loginResult.getAccessToken().getDeclinedPermissions() + "");
                Log.d("FB loginResult ", "Access Token getExpires()                = " + loginResult.getAccessToken().getExpires() + "");
                Log.d("FB loginResult ", "Access Token getLastRefresh()            = " + loginResult.getAccessToken().getLastRefresh() + "");
                Log.d("FB loginResult ", "Access Token getPermissions()            = " + loginResult.getAccessToken().getPermissions() + "");
                Log.d("FB loginResult ", "Access Token getSource()                 = " + loginResult.getAccessToken().getSource() + "");
                Log.d("FB loginResult ", "Access Token isExpired()                 = " + loginResult.getAccessToken().isExpired() + "");

                // If Facebook login operation is successfull then send a register request to API for saving this user or if he/she already registered just perform a login operation.
                final Handler loginRequestHandler = new Handler();
                Thread loginRequestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        IUserDAO userDAO = new UserDAOImpl();
                        final DAOResponse daoResponse = userDAO.saveUser(loginResult.getAccessToken().getUserId());

                        // Handling server response for register/login request.
                        loginRequestHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (daoResponse.getError().getErrorCode() == 0) {
                                    // Success.
                                    UserModel userModel = (UserModel) daoResponse.getObject();

                                    // Store User data locally but temporary.
                                    BaseApplication.setUserModel(userModel);

                                    startActivity(new Intent(WalkthroughActivity.this, ShoppingCartActivity.class));
                                    WalkthroughActivity.this.finish();

                                } else {
                                    // Check if the error code is a Http status code.
                                    HttpFailStatusCodeHandler httpFailStatusCodeHandler = new HttpFailStatusCodeHandler(WalkthroughActivity.this);
                                    if (!httpFailStatusCodeHandler.handleCode(daoResponse.getError().getErrorCode())) {
                                        // Error code isn't a Http status code, then it should be an API error code. So handle it.
                                        APIErrorCodeHandler apiErrorCodeHandler = new APIErrorCodeHandler(WalkthroughActivity.this);
                                        apiErrorCodeHandler.handleErrorCode(daoResponse.getError().getErrorCode());
                                    }

                                }

                            }
                        });
                    }
                });
                loginRequestThread.start();

            }

            /**
             * Facebook Login Activity opened but user pressed back and canceled the process.
             */
            @Override
            public void onCancel() {
                Toast.makeText(WalkthroughActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            /**
             * A FacebookException occured during the logging in.
             * @param exception A FacebookException instance.
             */
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(WalkthroughActivity.this, "An error occured when logging in", Toast.LENGTH_SHORT).show();
                Log.e("FacebookException ", exception.toString());
            }

        });

        // Load all fragments.
        viewPager.setOffscreenPageLimit(2);

        setUpViewPager();

        nextButton.setOnClickListener(nextButtonOnClickListener);
        facebookLogin.setOnClickListener(facebookLoginButtonOnClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Facebook callbackManager.onActivityResult() should be called here.
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Creates WalkthroughModel with title and description texts and uses these model on creating walkthrough fragments.
     * Arranges the FragmentStatePagerAdapter with those Fragments and sets up the adapter to ViewPager.
     */
    private void setUpViewPager() {

        // This container will be responsible for holding page number indicator balls.
        LinearLayout walkthroughBallContainer = (LinearLayout) findViewById(R.id.walkthrough_ball_container);
        LinearLayout.LayoutParams params;

        // Converting 5dp to its pixel equivalence with respect to current device's density.
        int fiveDpAsPx = (int) (5 * Resources.getSystem().getDisplayMetrics().density);

        // Creating new WalkthroughModels and adding them to ArrayList.
        ArrayList<WalkthroughModel> walkthroughModelList = new ArrayList<>();
        walkthroughModelList.add(new WalkthroughModel(R.string.walkthrough_title_1, R.string.walkthrough_description_1));
        walkthroughModelList.add(new WalkthroughModel(R.string.walkthrough_title_2, R.string.walkthrough_description_2));
        walkthroughModelList.add(new WalkthroughModel(R.string.walkthrough_title_3, R.string.walkthrough_description_3));

        numberOfViewPagerChildren = walkthroughModelList.size();

        // Creating WalkthroughContainerFragments with WalkthroughModels.
        for (int a = 0; a < numberOfViewPagerChildren; a++) {
            walkthroughFragmentList.add(a, newInstanceOfWalkthroughContainerFragment(walkthroughModelList.get(a).getTitleText(), walkthroughModelList.get(a).getDescriptionText()));
        }

        // Page number indicator ball size will be 5dp x 5dp.
        params = new LinearLayout.LayoutParams(fiveDpAsPx, fiveDpAsPx);
        // Setting margins between them.
        params.setMargins(4, 0, 4, 0);

        ballList = new ArrayList<>();

        // Setting ball's background. (First one is different from others.)
        for (int a = 0; a < numberOfViewPagerChildren; a++) {
            View ball = new View(this);
            ball.setLayoutParams(params);
            if (a == 0) {
                ball.setBackground(ContextCompat.getDrawable(WalkthroughActivity.this, R.drawable.walkthrough_ball_focus));
            } else {
                ball.setBackground(ContextCompat.getDrawable(WalkthroughActivity.this, R.drawable.walkthrough_ball_blur));
            }
            walkthroughBallContainer.addView(ball);
            ballList.add(a, ball);
        }

        // Setting adapter to ViewPager.
        viewPager.setAdapter(new WalkthroughViewPagerAdapter(getSupportFragmentManager()));

        // Listening page changes for arranging page number indicators.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int previouslySelected = 0;

            @Override
            public void onPageScrolled(int i, float v, int i2) {}

            @Override
            public void onPageSelected(int i) {
                // When current page changed, page number indicator ball backgrounds will be re-arranged.

                View ball;
                // Get the previously selected ball and change it's background to "not selected".
                ball = ballList.get(previouslySelected);
                ball.setBackground(ContextCompat.getDrawable(WalkthroughActivity.this, R.drawable.walkthrough_ball_blur));

                // Get the current ball and change it's background to "selected".
                ball = ballList.get(i);
                ball.setBackground(ContextCompat.getDrawable(WalkthroughActivity.this, R.drawable.walkthrough_ball_focus));

                // Previously selected ball will be the current one when the next page change occured.
                previouslySelected = i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {}

        });

        // Setting a new PageTransformer to ViewPager for getting a page change animation-like effect.
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {

                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                } else if (position <= 1) { // [-1,1]
                    view.findViewById(R.id.walkthrough_title).setTranslationX((position) * (pageWidth / 2));
                    view.findViewById(R.id.walkthrough_description).setTranslationX((position) * (pageWidth / 4));
                } else { // (1,+Infinity]
                }

            }
        });

    }

    /**
     * Creates a new WalkthroughContainerFragment with given strings.
     * @param titleText Will be "title" on WalkthroughContainerFragment when it is created.
     * @param descriptionText Will be "description" on WalkthroughContainerFragment when it is created.
     * @return A new WalkthroughContainerFragment.
     */
    private WalkthroughContainerFragment newInstanceOfWalkthroughContainerFragment(int titleText, int descriptionText) {
        WalkthroughContainerFragment fragment = new WalkthroughContainerFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("title_text"          , titleText);
        bundle.putInt("description_text"    , descriptionText);
        fragment.setArguments(bundle);
        return fragment;
    }

    // A custom FragmentStatePagerAdapter for giving it to ViewPager.
    class WalkthroughViewPagerAdapter extends FragmentStatePagerAdapter {

        public WalkthroughViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return walkthroughFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return numberOfViewPagerChildren;
        }

    }

}
