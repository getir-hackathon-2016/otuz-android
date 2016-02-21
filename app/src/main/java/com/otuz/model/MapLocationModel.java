package com.otuz.model;

/**
 * A POJO for holding a location point and its address details.
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
public class MapLocationModel {

    private double  latitude,
                    longitude;
    private String  formattedAddress;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}