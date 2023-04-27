package ru.mvrlrd.playlistmaker.settings.data

interface IThemeStorage {
    fun switch(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
}