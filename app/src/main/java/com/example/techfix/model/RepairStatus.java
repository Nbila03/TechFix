package com.example.techfix.model;
public class RepairStatus {

    public static final String SUBMITTED = "SUBMITTED";
    public static final String BRANCH_ASSIGNED = "BRANCH_ASSIGNED";
    public static final String TECHNICIAN_ASSIGNED = "TECHNICIAN_ASSIGNED";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String READY_FOR_COLLECTION = "READY_FOR_COLLECTION";
    public static final String COMPLETED = "COMPLETED";

    public static final String[] STAGE_ORDER = {
            SUBMITTED, BRANCH_ASSIGNED, TECHNICIAN_ASSIGNED,
            IN_PROGRESS, READY_FOR_COLLECTION, COMPLETED
    };

    private RepairStatus() {

    }
    public static int indexOf(String status) {
        for (int i = 0; i < STAGE_ORDER.length; i++) {
            if (STAGE_ORDER[i].equalsIgnoreCase(status)) return i;
        }
        return -1;
    }

    /** Friendly labeL*/
    public static String label(String status) {
        if (status == null) return "";
        String[] words = status.replace('_', ' ').toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.isEmpty()) continue;
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}