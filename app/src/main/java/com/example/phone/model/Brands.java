package com.example.phone.model;

public class Brands {

    private int brandId;
    private String brandName;
    private String imgBrand;

    public Brands() {

    }

    public Brands(int cateId, String cateName, String imgCate) {
        this.brandId = cateId;
        this.brandName = cateName;
        this.imgBrand = imgCate;
    }


    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setImgBrand(String imgBrand) {
        this.imgBrand = imgBrand;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getImgBrand() {
        return imgBrand;
    }
}
