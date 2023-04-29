package ru.mvrlrd.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeSwitchRepositoryImpl(private val themeStorage: IThemeStorage):ThemeSwitchRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
        applyCurrentTheme()
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}