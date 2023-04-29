package ru.mvrlrd.playlistmaker.settings.domain

interface ThemeSwitchInteractor {
    fun switch(isDarkModeOn: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}