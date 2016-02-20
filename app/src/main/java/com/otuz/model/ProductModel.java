package com.otuz.model;

/**
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ProductModel {

    private String   name,
                    photoUrl,
                    barcodeNumber,
                    price,
                    quantity;

    private ProductId productId = new ProductId();

    class ProductId{
        String id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId.id;
    }

    public void setProductId(String productId) {
        this.productId.id = productId;
    }

}
