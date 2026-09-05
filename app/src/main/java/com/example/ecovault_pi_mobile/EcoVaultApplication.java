package com.example.ecovault_pi_mobile;

import android.app.Application;

import com.example.ecovault_pi_mobile.utils.ThemeHelper;

public class EcoVaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeHelper.applySavedTheme(this);
    }
}
