package ru.mvrlrd.playlistmaker.mediateka.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoriteAdapter
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesFragment
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.FavoritesRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.impl.FavoriteInteractorImpl
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistsViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.data.PlaylistRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistRepository
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.impl.PlaylistInteractorImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.ui.AddPlaylistViewModel

val mediatekaVMModule = module {
    viewModel {
        FavoritesViewModel(favoriteInteractor = get())
    }
    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        AddPlaylistViewModel(interactor = get())
    }
    factory {
        FavoriteAdapter()
    }

    factory {
        FavoritesFragment()
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get() )
    }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(favoriteDb = get(), trackConverter = get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(dataBase = get(), converter = get())
    }
    single <PlaylistInteractor> {
        PlaylistInteractorImpl(repository = get())
    }

}