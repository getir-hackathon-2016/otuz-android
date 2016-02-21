package com.otuz.model;

/**
 * A POJO for walkthrough fragments.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class WalkthroughModel {

    private int titleText;
    private int descriptionText;

    public WalkthroughModel(int titleText, int descriptionText){
        this.titleText        = titleText;
        this.descriptionText  = descriptionText;
    }

    public int getTitleText() {
        return titleText;
    }

    public void setTitleText(int titleText) {
        this.titleText = titleText;
    }

    public int getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(int descriptionText) {
        this.descriptionText = descriptionText;
    }

}
