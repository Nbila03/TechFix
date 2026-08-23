package com.example.techfix.branch;

import com.example.techfix.model.RepairRequest;
import com.example.techfix.model.RepairStatus;

import java.util.ArrayList;
import java.util.List;

// TEMPORARY
class DemoRepairData {

    private DemoRepairData() { }

    static List<RepairRequest> getAllRepairs() {
        List<RepairRequest> all = new ArrayList<>();

        RepairRequest r1 = new RepairRequest();
        r1.setRepairId(1024);
        r1.setDeviceId(1);
        r1.setDeviceName("iPhone 13");
        r1.setServiceName("Screen Replacement");
        r1.setBranchName("TechFix Colombo");
        r1.setStatus(RepairStatus.COMPLETED);
        r1.setFinalCost(25000);
        all.add(r1);

        RepairRequest r2 = new RepairRequest();
        r2.setRepairId(1031);
        r2.setDeviceId(1);
        r2.setDeviceName("iPhone 13");
        r2.setServiceName("Battery Replacement");
        r2.setBranchName("TechFix Colombo");
        r2.setStatus(RepairStatus.IN_PROGRESS);
        r2.setEstimatedCost(8000);
        all.add(r2);

        RepairRequest r3 = new RepairRequest();
        r3.setRepairId(1040);
        r3.setDeviceId(2);
        r3.setDeviceName("Dell XPS 13");
        r3.setServiceName("Keyboard Repair");
        r3.setStatus(RepairStatus.SUBMITTED);
        r3.setEstimatedCost(6000);
        all.add(r3);

        return all;
    }

    static List<RepairRequest> getRepairsForDevice(int deviceId) {
        List<RepairRequest> filtered = new ArrayList<>();
        for (RepairRequest r : getAllRepairs()) {
            if (r.getDeviceId() == deviceId) filtered.add(r);
        }
        return filtered;
    }
}