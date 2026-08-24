package com.example.techfix.branch;

import com.example.techfix.model.RepairRequest;
import com.example.techfix.model.RepairStatus;

import java.util.ArrayList;
import java.util.List;

// Temporary demo repair data used for testing the repair screens.
class DemoRepairData {

    private DemoRepairData() {
    }

    // Creates a list of sample repairs.
    static List<RepairRequest> getAllRepairs() {

        List<RepairRequest> repairs =
                new ArrayList<>();

        // Repair 1 - Completed
        RepairRequest repair1 =
                new RepairRequest();

        repair1.setRepairId(1024);
        repair1.setDeviceId(1);
        repair1.setDeviceName("iPhone 13");
        repair1.setServiceName("Screen Replacement");
        repair1.setBranchName("TechFix Colombo");
        repair1.setStatus(RepairStatus.COMPLETED);
        repair1.setFinalCost(25000);

        repairs.add(repair1);

        // Repair 2 - In Progress
        RepairRequest repair2 =
                new RepairRequest();

        repair2.setRepairId(1031);
        repair2.setDeviceId(1);
        repair2.setDeviceName("iPhone 13");
        repair2.setServiceName("Battery Replacement");
        repair2.setBranchName("TechFix Colombo");
        repair2.setStatus(RepairStatus.IN_PROGRESS);
        repair2.setEstimatedCost(8000);

        repairs.add(repair2);

        // Repair 3 - Submitted
        RepairRequest repair3 =
                new RepairRequest();

        repair3.setRepairId(1040);
        repair3.setDeviceId(2);
        repair3.setDeviceName("Dell XPS 13");
        repair3.setServiceName("Keyboard Repair");
        repair3.setStatus(RepairStatus.SUBMITTED);
        repair3.setEstimatedCost(6000);

        repairs.add(repair3);

        return repairs;
    }

    // Returns only the repairs belonging to a specific device.
    static List<RepairRequest> getRepairsForDevice(
            int deviceId) {

        List<RepairRequest> filteredRepairs =
                new ArrayList<>();

        List<RepairRequest> allRepairs =
                getAllRepairs();

        for (RepairRequest repair : allRepairs) {

            if (repair.getDeviceId() == deviceId) {

                filteredRepairs.add(repair);
            }
        }

        return filteredRepairs;
    }
}