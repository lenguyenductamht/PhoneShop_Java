package com.example.phone.model;

import java.io.Serializable;

public class Orders implements Serializable {
    private String userId;
    private String orderId;
    private String orderDate;
    private String discountId;
    private double total;
    public Orders() {
    }

    public Orders(String userId, String orderId, String orderDate, String discountId, double total){
        this.userId = userId;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.discountId = discountId;
        this.total = total;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    // dùng để thêm dữ liệu hoặc cập nhật lên firebase


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
