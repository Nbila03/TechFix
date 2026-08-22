package com.example.techfix.model;
public class Technician {
    private int technicianId;
    private int branchId;
    private String technicianName;
    private String specialization;
    private String phone;
    private boolean available;
    public Technician() { }

    public Technician(int technicianId, int branchId, String technicianName,
                      String specialization, String phone, boolean available) {
        this.technicianId = technicianId;
        this.branchId = branchId;
        this.technicianName = technicianName;
        this.specialization = specialization;
        this.phone = phone;
        this.available = available;
    }

    public int getTechnicianId() { return technicianId; }
    public void setTechnicianId(int technicianId) { this.technicianId = technicianId; }
    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

}


