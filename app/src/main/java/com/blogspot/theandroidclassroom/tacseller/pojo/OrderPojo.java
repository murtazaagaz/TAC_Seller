package com.blogspot.theandroidclassroom.tacseller.pojo;

/**
 * Created by Murtaza on 1/15/2017.
 */

public class OrderPojo {
    private String image, status, productName, productPrize;

    public OrderPojo(){

    }

    public OrderPojo(String image, String status, String productName, String productPrize) {
        this.image = image;
        this.status = status;
        this.productName = productName;
        this.productPrize = productPrize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(String productPrize) {
        this.productPrize = productPrize;
    }
}
