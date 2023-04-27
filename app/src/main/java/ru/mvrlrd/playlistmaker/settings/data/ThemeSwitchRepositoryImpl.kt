package ru.mvrlrd.playlistmaker.settings.data

import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeSwitchRepositoryImpl(private val themeStorage: IThemeStorage):ThemeSwitchRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.isDarkModeOn()
    }


}