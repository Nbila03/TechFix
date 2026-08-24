package com.example.techfix.location;

import android.location.Location;
import com.example.techfix.model.Branch;
import java.util.ArrayList;
import java.util.List;

public class BranchAssignmentHelper {

    public interface TechnicianAvailabilityCheck {
        boolean isTechnicianAvailable(int branchId);
    }

    public interface PartAvailabilityCheck {
        boolean isPartAvailable(int branchId, int requiredPartId);
    }

    public static Branch findNearestEligibleBranch(
            double customerLat, double customerLng,
            List<Branch> allBranches,
            int requiredPartId,
            TechnicianAvailabilityCheck techCheck,
            PartAvailabilityCheck partCheck) {

        List<Branch> eligible = new ArrayList<>();

        for (Branch branch : allBranches) {
            if (!branch.isActive()) {
                continue; // inactive branches are never considered
            }

            boolean techOk = techCheck.isTechnicianAvailable(branch.getBranchId());
            boolean partOk = requiredPartId < 0
                    || partCheck.isPartAvailable(branch.getBranchId(), requiredPartId);

            branch.setTechnicianAvailable(techOk);
            branch.setPartAvailable(requiredPartId < 0 || partOk);

            float[] results = new float[1];
            Location.distanceBetween(
                    customerLat, customerLng,
                    branch.getLatitude(), branch.getLongitude(),
                    results);
            branch.setDistanceKm(results[0] / 1000f);

            if (techOk && (requiredPartId < 0 || partOk)) {
                eligible.add(branch);
            }
        }

        Branch nearest = null;
        for (Branch b : eligible) {
            if (nearest == null || b.getDistanceKm() < nearest.getDistanceKm()) {
                nearest = b;
            }
        }
        return nearest;
    }
    public static List<Branch> sortByDistance(double customerLat, double customerLng, List<Branch> branches) {
        List<Branch> result = new ArrayList<>(branches);
        for (Branch b : result) {
            float[] out = new float[1];
            Location.distanceBetween(customerLat, customerLng, b.getLatitude(), b.getLongitude(), out);
            b.setDistanceKm(out[0] / 1000f);
        }
        result.sort((a, b) -> Float.compare(a.getDistanceKm(), b.getDistanceKm()));
        return result;
    }
}
