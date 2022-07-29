package com.example.phone.model;

import java.io.Serializable;

public class Discounts implements Serializable{
    private String discountId;
    private String title;
    private String startDate;
    private String endDate;
    private double percentage;
    private String description;
    private int maxQuantity;
    private int minQuantity;
    private String image;
    private int cateId;


    public Discounts() {
    }

    public Discounts(String discountId, String title, String startDate, String endDate,
                     double percentage, String description, int maxQuantity, int minQuantity,
                     int cateId, String image) {
        this.discountId = discountId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.minQuantity = minQuantity;
        this.cateId = cateId;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }
}
