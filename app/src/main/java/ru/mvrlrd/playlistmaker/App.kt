package ru.mvrlrd.playlistmaker

import android.app.Application
import android.util.Log
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.playlistmaker.settings.di.dataModule
import ru.mvrlrd.playlistmaker.settings.di.interactorModule
import ru.mvrlrd.playlistmaker.settings.di.repositoryModule
import ru.mvrlrd.playlistmaker.settings.di.viewModelModule
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
        val themeSwitcherInteractor  : ThemeSwitchInteractor by inject()
        themeSwitcherInteractor.applyCurrentTheme()
    }
}

