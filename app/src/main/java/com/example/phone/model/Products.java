package com.example.phone.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Products implements Serializable {
    private int productId;
    private int brandId;
    private int price;
    private String imgProduct;
    private String productName;
    private String description;
    private String backCam;
    private String battery;
    private String chip;
    private String frontCam;
    private String ram;
    private String rom;
    private String screen;
    private String sim;
    private String OS;
    private int quantity;


    public Products() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public Products(int productId, int cateId, int price, String imgProduct, String productName, String description, String backCam, String battery, String chip, String frontCam, String ram, String rom, String sreen, String sreenSize, String sim, String OS) {
        this.productId = productId;
        this.brandId = cateId;
        this.price = price;
        this.imgProduct = imgProduct;
        this.productName = productName;
        this.description = description;
        this.backCam = backCam;
        this.battery = battery;
        this.chip = chip;
        this.frontCam = frontCam;
        this.ram = ram;
        this.rom = rom;
        this.screen = sreen;
        this.sim = sim;
        this.OS = OS;
    }
    public Products(int productId, int cateId, int price, String imgProduct,
                    String productName, String description) {
        this.productId = productId;
        this.brandId = cateId;
        this.price = price;
        this.imgProduct = imgProduct;
        this.productName = productName;
        this.description = description;
    }
    public Products(int price, String productName, String imgProduct){
        this.price = price;
        this.productName = productName;
        this.imgProduct = imgProduct;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cateId", brandId);
        result.put("description", description);
        result.put("imgProduct", imgProduct);
        result.put("price", price);
        result.put("productID", productId);
        result.put("productName", productName);
        result.put("backCam", backCam);
        result.put("battery", battery);
        result.put("chip", chip);
        result.put("frontCam", frontCam);
        result.put("ram", ram);
        result.put("rom", rom);
        result.put("screen", screen);
        result.put("sim", sim);
        result.put("OS", OS);
        result.put("quantity", quantity);

        return result;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public int getProductId() {
        return productId;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getPrice() {
        return price;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackCam() {
        return backCam;
    }

    public void setBackCam(String backCam) {
        this.backCam = backCam;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public String getFrontCam() {
        return frontCam;
    }

    public void setFrontCam(String frontCam) {
        this.frontCam = frontCam;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }
}
