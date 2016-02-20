package com.otuz.controller.Activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import com.otuz.R;
import com.otuz.controller.Fragment.WalkthroughContainerFragment;
import com.otuz.model.WalkthroughModel;
import java.util.ArrayList;

/**
 * An Activity for helping not logged-in users.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class WalkthroughActivity extends AppCompatActivity {

    // For holding walkthrough pages.
    private ViewPager viewPager;
    // Number of walkthrough pages.
    private int numberOfViewPagerChildren;
    // An ArrayList which holds walkthrough pages (fragment) for giving them to ViewPager's FragmentStatePagerAdapter.
    private ArrayList<Fragment> walkthroughFragmentList = new ArrayList<>();
    // An ArrayList which holds oval backgrounded View's. These are page number indicators.
    private ArrayList<View> ballList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        viewPager = (ViewPager) findViewById(R.id.walkthrough_view_pager);
        // Load all fragments.
        viewPager.setOffscreenPageLimit(2);

        setUpViewPager();

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
                ball.setBackgroundResource(R.drawable.walkthrough_ball_focus);
            } else {
                ball.setBackgroundResource(R.drawable.walkthrough_ball_blur);
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
                ball.setBackgroundResource(R.drawable.onboarding_ball_blur);

                // Get the current ball and change it's background to "selected".
                ball = ballList.get(i);
                ball.setBackgroundResource(R.drawable.onboarding_ball_focus);

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
