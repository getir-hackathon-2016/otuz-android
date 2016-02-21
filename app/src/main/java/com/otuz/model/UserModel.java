package com.otuz.model;

import java.util.ArrayList;

/**
 * A POJO for user details.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class UserModel {

    private String  id,
                    facebookUserId,
                    registeredAt;
    private AddressModel addressModel;
    private ArrayList<ProductModel> userProducts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public ArrayList<ProductModel> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(ArrayList<ProductModel> userProducts) {
        this.userProducts = userProducts;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
