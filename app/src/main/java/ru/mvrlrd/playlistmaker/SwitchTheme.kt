package ru.mvrlrd.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val IS_DARK_MODE_ON = "is_dark_mode_on"

class SwitchTheme(private val sharedPreferences: SharedPreferences) {

    fun switch(darkThemeEnabled:Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(IS_DARK_MODE_ON, darkThemeEnabled)
            .apply()
        applyTheme()
    }

    fun applyTheme(): Boolean{
        val darkThemeEnabled = sharedPreferences.getBoolean(IS_DARK_MODE_ON, false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        return darkThemeEnabled
    }
}