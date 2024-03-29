package ru.mvrlrd.playlistmaker

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mvrlrd.playlistmaker.mediateka.favorites.di.favoritesDataModule
import ru.mvrlrd.playlistmaker.mediateka.favorites.di.favoritesInteractorModule
import ru.mvrlrd.playlistmaker.mediateka.favorites.di.favoritesRepositoryModule
import ru.mvrlrd.playlistmaker.mediateka.favorites.di.favoritesUiModule
import ru.mvrlrd.playlistmaker.mediateka.favorites.di.favoritesViewModelModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di.addPlaylistRepositoryModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di.addPlaylistUseCaseModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di.addPlaylistViewModelModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.di.internalStorageRepositoryModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.di.internalStorageUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di.updatePlaylistRepository
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di.updatePlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di.updatePlaylistViewModelModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di.playlistInfoInteractorModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di.playlistInfoRepositoryModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di.playlistInfoVMModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di.playlistsDataModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di.playlistsInteractorModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di.playlistsRepositoryModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di.playlistsUiModule
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di.playlistsViewModelModule
import ru.mvrlrd.playlistmaker.player.di.*
import ru.mvrlrd.playlistmaker.search.di.*
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
                playerViewModelModule,
                playerUiModule
            )
            modules(
                searchNetworkDataModule,
                searchLocalStorageDataModule,
                searchRepositoryModule,
                searchInteractorModule,
                searchViewModelModule,
                searchUiModule
            )
            modules(
                playlistsDataModule,
                playlistsRepositoryModule,
                playlistsInteractorModule,
                playlistsViewModelModule,
                playlistsUiModule
            )
            modules(
                favoritesDataModule,
                favoritesRepositoryModule,
                favoritesInteractorModule,
                favoritesViewModelModule,
                favoritesUiModule
            )
            modules(
                addPlaylistRepositoryModule,
                addPlaylistUseCaseModule,
                addPlaylistViewModelModule
            )
            modules(
                playlistInfoRepositoryModule,
                playlistInfoInteractorModule,
                playlistInfoVMModule
            )
            modules(
                updatePlaylistRepository,
                updatePlaylistUseCase,
                updatePlaylistViewModelModule
            )
            modules(
                internalStorageRepositoryModule,
                internalStorageUseCase,
            )
        }
        val themeSwitcherInteractor: ThemeSwitchInteractor by inject()
        themeSwitcherInteractor.applyCurrentTheme()
    }
}

