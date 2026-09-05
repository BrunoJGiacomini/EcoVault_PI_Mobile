package com.example.ecovault_pi_mobile.model;

import java.util.List;

public class GuideCategoryItem {
    private int iconRes;
    private int iconTintColorRes;
    private int bgColorRes;
    private String title;
    private String description;
    private List<String> tags;

    public GuideCategoryItem(int iconRes, int iconTintColorRes, int bgColorRes,
                             String title, String description, List<String> tags) {
        this.iconRes = iconRes;
        this.iconTintColorRes = iconTintColorRes;
        this.bgColorRes = bgColorRes;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public int getIconRes() { return iconRes; }
    public int getIconTintColorRes() { return iconTintColorRes; }
    public int getBgColorRes() { return bgColorRes; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getTags() { return tags; }
}
