package com.example.ecovault_pi_mobile.model;

public class CategoryItem {
    private String id;
    private String label;
    private String points;
    private int iconRes;
    private boolean active;

    public CategoryItem(String id, String label, String points, int iconRes, boolean active) {
        this.id = id;
        this.label = label;
        this.points = points;
        this.iconRes = iconRes;
        this.active = active;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getPoints() { return points; }
    public int getIconRes() { return iconRes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
