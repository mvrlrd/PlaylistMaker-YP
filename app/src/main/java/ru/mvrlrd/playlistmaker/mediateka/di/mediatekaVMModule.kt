package ru.mvrlrd.playlistmaker.mediateka.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistsViewModel
import ru.mvrlrd.playlistmaker.search.ui.SearchViewModel

val mediatekaVMModule = module {
    viewModel {
        FavoritesViewModel()
    }
    viewModel {
        PlaylistsViewModel()
    }
}