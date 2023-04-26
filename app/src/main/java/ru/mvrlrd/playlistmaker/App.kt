package ru.mvrlrd.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import ru.mvrlrd.playlistmaker.util.SwitchTheme


class App : Application() {
    private lateinit var themePreferences: SharedPreferences
    private lateinit var switchTheme: SwitchTheme

    override fun onCreate() {
        super.onCreate()
        themePreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        switchTheme = SwitchTheme(themePreferences)
        applySavedThemeMode()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        switchTheme.switch(darkThemeEnabled)
    }
    fun applySavedThemeMode(){
        switchTheme.applyTheme()
    }
    companion object{
        private const val THEME_PREFERENCES = "current_theme"
    }
}
