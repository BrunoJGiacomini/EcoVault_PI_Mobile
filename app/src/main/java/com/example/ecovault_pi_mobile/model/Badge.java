package com.example.ecovault_pi_mobile.model;

public class Badge {
    private String emoji;
    private String label;
    private boolean unlocked;

    public Badge(String emoji, String label, boolean unlocked) {
        this.emoji = emoji;
        this.label = label;
        this.unlocked = unlocked;
    }

    public String getEmoji() { return emoji; }
    public String getLabel() { return label; }
    public boolean isUnlocked() { return unlocked; }
}
