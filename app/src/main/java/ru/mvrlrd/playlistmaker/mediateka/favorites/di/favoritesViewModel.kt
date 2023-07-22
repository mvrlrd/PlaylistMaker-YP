package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesViewModel

val favoritesViewModelModule = module {
    viewModel {
        FavoritesViewModel(favoriteInteractor = get())
    }
}