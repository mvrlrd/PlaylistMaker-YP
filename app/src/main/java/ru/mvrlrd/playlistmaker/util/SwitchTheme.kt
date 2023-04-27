package ru.mvrlrd.playlistmaker.util

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class SwitchTheme(private val sharedPreferences: SharedPreferences) {

    fun switch(darkThemeEnabled:Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(IS_DARK_MODE_ON, darkThemeEnabled)
            .apply()
        applyTheme()
    }

    fun applyTheme(){
        val darkThemeEnabled = sharedPreferences.getBoolean(IS_DARK_MODE_ON, false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkModeOn():Boolean{
        return sharedPreferences.getBoolean(IS_DARK_MODE_ON, false)
    }
    companion object{
        private const val IS_DARK_MODE_ON = "is_dark_mode_on"
    }
}