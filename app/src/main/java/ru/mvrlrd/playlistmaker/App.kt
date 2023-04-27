package ru.mvrlrd.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import ru.mvrlrd.playlistmaker.settings.SwitchTheme


class App : Application(), ThemeSwitcher {
    private lateinit var themePreferences: SharedPreferences
    private lateinit var switchTheme: SwitchTheme

    override fun onCreate() {
        super.onCreate()
        themePreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        switchTheme = SwitchTheme(themePreferences)
        applySavedThemeMode()
    }

    private fun applySavedThemeMode() {
        switchTheme.applyTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        switchTheme.switch(darkThemeEnabled)
    }

    override fun isDarkModeOn(): Boolean {
        return switchTheme.isDarkModeOn()
    }

    companion object {
        private const val THEME_PREFERENCES = "current_theme"
    }
}

interface ThemeSwitcher{
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkModeOn():Boolean
}
