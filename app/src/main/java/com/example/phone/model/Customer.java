package com.example.phone.model;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String ID;
    private String phoneNumber;
    private String name;
    private String email;
    private String address;

    public Customer() {
    }

    public Customer(String ID, String phoneNumber, String name, String email, String address) {
        this.ID = ID;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    // dùng để thêm dữ liệu hoặc cập nhật lên firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("name", name);
        result.put("phoneNumber", getPhoneNumber());
        result.put("ID", ID);
        result.put("email", email);

//        result.put("")
        return result;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




}
