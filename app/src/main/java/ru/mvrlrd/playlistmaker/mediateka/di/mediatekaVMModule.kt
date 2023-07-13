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

val mediatekaVMModule = module {
    viewModel {
        FavoritesViewModel(favoriteInteractor = get())
    }
    viewModel {
        PlaylistsViewModel()
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
}