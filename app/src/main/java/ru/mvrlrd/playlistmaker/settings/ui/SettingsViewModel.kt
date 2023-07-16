package ru.mvrlrd.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor

class SettingsViewModel(private val themeSwitcherInteractor: ThemeSwitchInteractor) : ViewModel() {
    fun switchTheme(isChecked: Boolean) {
        themeSwitcherInteractor.switch(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return themeSwitcherInteractor.isDarkModeOn()
    }
}