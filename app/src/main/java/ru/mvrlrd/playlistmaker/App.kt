package ru.mvrlrd.playlistmaker

import android.app.Application
import ru.mvrlrd.playlistmaker.di.Creator
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor

class App : Application(), ThemeSwitcher {
    private lateinit var themeSwitcherInteractor: ThemeSwitchInteractor

    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.applyCurrentTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeSwitcherInteractor.switch(darkThemeEnabled)
    }

    override fun isDarkModeOn(): Boolean {
        return themeSwitcherInteractor.isDarkModeOn()
    }
}

interface ThemeSwitcher{
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkModeOn():Boolean
}
