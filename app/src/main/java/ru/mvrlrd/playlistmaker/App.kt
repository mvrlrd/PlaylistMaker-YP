package ru.mvrlrd.playlistmaker

import android.app.Application
import android.content.SharedPreferences

private const val THEME_PREFERENCES = "current_theme"

class App : Application() {
    private lateinit var themePreferences: SharedPreferences
    private lateinit var switchTheme: SwitchTheme

    override fun onCreate() {
        super.onCreate()
        themePreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        switchTheme = SwitchTheme(themePreferences)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        switchTheme.switch(darkThemeEnabled)
    }
    fun applySavedThemeMode(): Boolean{
        return switchTheme.applyTheme()
    }
}
