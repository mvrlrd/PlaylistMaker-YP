package ru.mvrlrd.playlistmaker

import android.app.Application
import ru.mvrlrd.playlistmaker.di.Creator
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor

class App : Application() {
    lateinit var themeSwitcherInteractor: ThemeSwitchInteractor

    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.applyCurrentTheme()
    }
}

