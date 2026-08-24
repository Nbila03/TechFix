package com.example.techfix.model;
public class RepairRequest {

    private int repairId;
    private int userId;
    private int deviceId;
    private String deviceName;      // for display
    private int serviceId;
    private String serviceName;     // for display
    private Integer branchId;       // null until a branch is assigned
    private String branchName;
    private Integer technicianId;
    private String problemDescription;
    private String appointmentDate;
    private String appointmentTime;
    private double estimatedCost;
    private double finalCost;
    private String status;          // SUBMITTED, BRANCH_ASSIGNED, TECHNICIAN_ASSIGNED, IN_PROGRESS, READY_FOR_COLLECTION, COMPLETED processes
    private String createdAt;

    public RepairRequest() { }

    public int getRepairId() { return repairId; }
    public void setRepairId(int repairId) { this.repairId = repairId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getDeviceId() { return deviceId; }
    public void setDeviceId(int deviceId) { this.deviceId = deviceId; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public Integer getTechnicianId() { return technicianId; }
    public void setTechnicianId(Integer technicianId) { this.technicianId = technicianId; }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }

    public double getFinalCost() { return finalCost; }
    public void setFinalCost(double finalCost) { this.finalCost = finalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}