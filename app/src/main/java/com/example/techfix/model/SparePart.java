package com.example.techfix.model;

public class SparePart {
    private int partId;
    private int branchId;
    private String partName;
    private String compatibleDevice;
    private int quantity;
    private double unitPrice;
    private boolean available;

    private int minimumStock;

    //constructr
    public SparePart() {

    }

    public SparePart(int partId, int branchId, String partName, String compatibleDevice, int quantity, double unitPrice, boolean available) {
        this.partId = partId;
        this.branchId = branchId;
        this.partName = partName;
        this.compatibleDevice = compatibleDevice;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.available = available;
    }

    public int getPartId() { return partId; }
    public void setPartId(int partId) { this.partId = partId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public String getCompatibleDevice() { return compatibleDevice; }
    public void setCompatibleDevice(String compatibleDevice) { this.compatibleDevice = compatibleDevice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    /** stock check: flag AND quantity both matter. */
    public boolean isInStock() {
        return available && quantity > 0;
    }

    public boolean isLowStock() {
        return quantity <= minimumStock;
    }
}
