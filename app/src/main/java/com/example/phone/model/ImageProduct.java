package com.example.phone.model;

public class ImageProduct {
    int productId;
    String img;

    public ImageProduct(String img, int productId){
        this.img = img;
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ImageProduct() {

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
