package ru.mvrlrd.playlistmaker.settings.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeSwitchInteractorImpl(private val themeSwitchRepository: ThemeSwitchRepository): ThemeSwitchInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        themeSwitchRepository.switchTheme(isDarkModeOn)
        applyCurrentTheme()
    }

    override fun isDarkModeOn(): Boolean {
        return themeSwitchRepository.isDarkModeOn()
    }

    override fun applyCurrentTheme(){
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}