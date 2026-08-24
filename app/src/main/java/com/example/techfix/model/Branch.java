package com.example.techfix.model;

public class Branch {

    private int branchId;
    private String branchName;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private String phone;
    private boolean active;

    // not stored in the table directly, filled in by BranchAssignmentHelper
    private float distanceKm = -1f;
    private boolean technicianAvailable = false;
    private boolean partAvailable = false;

    public Branch() { }

    public Branch(int branchId, String branchName, String address, String city,
                  double latitude, double longitude, String phone, boolean active) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.active = active;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public float getDistanceKm() { return distanceKm; }
    public void setDistanceKm(float distanceKm) { this.distanceKm = distanceKm; }

    public boolean isTechnicianAvailable() { return technicianAvailable; }
    public void setTechnicianAvailable(boolean technicianAvailable) { this.technicianAvailable = technicianAvailable; }

    public boolean isPartAvailable() { return partAvailable; }
    public void setPartAvailable(boolean partAvailable) { this.partAvailable = partAvailable; }

    // a branch only counts as eligible if it's active AND has a technician AND has the part
    public boolean isEligible() {
        return active && technicianAvailable && partAvailable;
    }
}