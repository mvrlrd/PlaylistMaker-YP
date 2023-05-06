package ru.mvrlrd.playlistmaker

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.playlistmaker.player.di.*
import ru.mvrlrd.playlistmaker.search.di.searchInteractorModule
import ru.mvrlrd.playlistmaker.search.di.searchLocalStorageDataModule
import ru.mvrlrd.playlistmaker.search.di.searchNetworkDataModule
import ru.mvrlrd.playlistmaker.search.di.searchRepositoryModule
import ru.mvrlrd.playlistmaker.search.di.searchViewModelModule
import ru.mvrlrd.playlistmaker.settings.di.*

import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                settingsDataModule,
                settingsInteractorModule,
                settingsRepositoryModule,
                settingsViewModelModule
            )
            modules(
                playerDataModule,
                playerInteractorModule,
                playerRepositoryModule,
                playerViewModelModule
            )
            modules(
                searchNetworkDataModule,
                searchLocalStorageDataModule,
                searchRepositoryModule,
                searchInteractorModule,
                searchViewModelModule
            )
        }
        val themeSwitcherInteractor: ThemeSwitchInteractor by inject()
        themeSwitcherInteractor.applyCurrentTheme()
    }
}

