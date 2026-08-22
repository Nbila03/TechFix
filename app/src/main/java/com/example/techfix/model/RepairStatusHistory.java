package com.example.techfix.model;

public class RepairStatusHistory {
    private int statusHistoryId;
    private int repairId;
    private String status;      // one of RepairStatus.STAGE_ORDER
    private String remarks;
    private String updatedAt;

    public RepairStatusHistory() { }

    public RepairStatusHistory(String status, String remarks, String updatedAt) {
        this.status = status;
        this.remarks = remarks;
        this.updatedAt = updatedAt;
    }

    public int getStatusHistoryId() { return statusHistoryId; }
    public void setStatusHistoryId(int statusHistoryId) { this.statusHistoryId = statusHistoryId; }

    public int getRepairId() { return repairId; }
    public void setRepairId(int repairId) { this.repairId = repairId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}