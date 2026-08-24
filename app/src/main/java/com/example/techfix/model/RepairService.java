package com.example.techfix.model;

public class RepairService {

    private String serviceId;
    private String name;
    private String description;
    private String price;
    private String estimatedDays;
    private int imageResId;

    // needed for Firestore to build this object back from a document
    public RepairService() {
    }

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
