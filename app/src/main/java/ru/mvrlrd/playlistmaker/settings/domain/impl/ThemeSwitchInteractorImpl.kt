package ru.mvrlrd.playlistmaker.settings.domain.impl

import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeSwitchInteractorImpl(private val themeSwitchRepository: ThemeSwitchRepository): ThemeSwitchInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        themeSwitchRepository.switchTheme(isDarkModeOn)
    }

    override fun isDarkModeOn(): Boolean {
        return themeSwitchRepository.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        themeSwitchRepository.applyCurrentTheme()
    }
}