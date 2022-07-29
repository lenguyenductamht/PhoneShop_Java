package com.example.phone.model;

import java.util.HashMap;
import java.util.Map;

public class UserInfor {
    private String userId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String imgUser;
    private String emailUser;
    private String passUser;

    public UserInfor() {
        // mặc định
    }

    public UserInfor(String fullName, String address, String phoneNumber, String email, String img) {

        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailUser = email;
        this.imgUser = img;

    }
    //    public UserInfor(String userId, String fullName, String address, String phoneNumber, String imgUser) {
//        this.userId = userId;
//        this.fullName = fullName;
//        this.address = address;
////        this.setPhoneNumber(phoneNumber);
//        this.setImgUser(imgUser);
//    }
    // dùng để thêm dữ liệu hoặc cập nhật lên firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("fullName", fullName);
        result.put("phoneNumber", getPhoneNumber());
        result.put("imgUser", imgUser);
        result.put("userID", userId);
//        result.put("")
        return result;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }


    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
