package ru.mvrlrd.playlistmaker.mediateka.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.favorites.FavoriteAdapter
import ru.mvrlrd.playlistmaker.mediateka.data.FavoritesRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.mediateka.domain.impl.FavoriteInteractorImpl
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistsViewModel
import ru.mvrlrd.playlistmaker.search.ui.SearchViewModel

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

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get() )
    }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(favoriteDb = get(), trackConverter = get())
    }
}