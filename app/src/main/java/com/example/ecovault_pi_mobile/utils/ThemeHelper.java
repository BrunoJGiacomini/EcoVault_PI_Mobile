package com.example.ecovault_pi_mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {

    private static final String PREFS_NAME = "ecovault_prefs";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    public static void applySavedTheme(Context context) {
        boolean isDark = isDarkModeEnabled(context);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static boolean isDarkModeEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public static void toggleTheme(Context context) {
        boolean isDark = isDarkModeEnabled(context);
        boolean novoValor = !isDark;

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, novoValor).apply();

        AppCompatDelegate.setDefaultNightMode(
                novoValor ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
