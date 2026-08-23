package com.example.techfix.model;

public class RepairService {

    private String name;
    private String description;
    private String price;
    private String estimatedDays;
    private int imageResId;

    public RepairService(
            String name,
            String description,
            String price,
            String estimatedDays,
            int imageResId
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.estimatedDays = estimatedDays;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getEstimatedDays() {
        return estimatedDays;
    }

    public int getImageResId() {
        return imageResId;
    }
}