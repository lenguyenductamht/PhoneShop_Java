package com.example.phone.model;

public class Cart {
    private int productId;
    private String productName;
    private String img;
    private int price;
    private int quantity;
    private int cateId;

    public Cart() {
    }

    public Cart(int productId, String productName, String img, int price,
                int quantity, int cateId) {
        this.productId = productId;
        this.productName = productName;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
        this.cateId = cateId;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImg() {
        return img;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
