package com.example.techfix.model;

public class RepairService {

    private String serviceId;
    private String name;
    private String description;
    private String price;
    private String estimatedDays;
    private int imageResId;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required for Firestore
    // =====================================================

    public RepairService() {
    }

    // =====================================================
    // FULL CONSTRUCTOR
    // Used when serviceId is available
    // =====================================================

    public RepairService(
            String serviceId,
            String name,
            String description,
            String price,
            String estimatedDays,
            int imageResId
    ) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.estimatedDays = estimatedDays;
        this.imageResId = imageResId;
    }

    // =====================================================
    // UI CONSTRUCTOR
    // Used by ServicesActivity for local/demo service data
    // =====================================================

    public RepairService(
            String name,
            String description,
            String price,
            String estimatedDays,
            int imageResId
    ) {
        this.serviceId = "";
        this.name = name;
        this.description = description;
        this.price = price;
        this.estimatedDays = estimatedDays;
        this.imageResId = imageResId;
    }

    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(String estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}