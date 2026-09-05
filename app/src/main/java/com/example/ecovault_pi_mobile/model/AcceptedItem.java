package com.example.ecovault_pi_mobile.model;

public class AcceptedItem {
    private String id;
    private String label;
    private int pointsPerDisposal;
    private int iconRes;

    public AcceptedItem(String id, String label, int pointsPerDisposal, int iconRes) {
        this.id = id;
        this.label = label;
        this.pointsPerDisposal = pointsPerDisposal;
        this.iconRes = iconRes;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public int getPointsPerDisposal() { return pointsPerDisposal; }
    public int getIconRes() { return iconRes; }
}
