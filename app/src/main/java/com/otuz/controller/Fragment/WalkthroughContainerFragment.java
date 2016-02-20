package com.otuz.controller.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otuz.R;

/**
 * Every instance of this class is one of the pages on the Walkthrough Activity.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class WalkthroughContainerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v                  = inflater.inflate(R.layout.fragment_walkthrough_container, container, false);
        TextView title          = (TextView)v.findViewById(R.id.walkthrough_title);
        TextView description    = (TextView)v.findViewById(R.id.walkthrough_description);

        // Get title and description string ids from arguments.
        int titleText       = getArguments().getInt("title_text");
        int descriptionText = getArguments().getInt("description_text");

        // Set them.
        title.setText(getResources().getString(titleText));
        description.setText(getResources().getString(descriptionText));

        return v;
    }

}