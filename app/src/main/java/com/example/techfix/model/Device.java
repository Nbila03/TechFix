package com.example.techfix.model;

public class Device {

    private int deviceId;
    private int userId;
    private int categoryId;

    private String deviceName;
    private String brand;
    private String model;
    private String serialNumber;

    public Device() {
    }

    public Device(int deviceId,
                  int userId,
                  int categoryId,
                  String deviceName,
                  String brand,
                  String model,
                  String serialNumber) {

        this.deviceId = deviceId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.deviceName = deviceName;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}