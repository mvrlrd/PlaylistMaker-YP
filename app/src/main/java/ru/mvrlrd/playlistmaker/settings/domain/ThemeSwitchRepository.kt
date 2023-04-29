package ru.mvrlrd.playlistmaker.settings.domain

interface ThemeSwitchRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}