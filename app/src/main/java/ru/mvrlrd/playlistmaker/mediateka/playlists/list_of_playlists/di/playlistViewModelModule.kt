package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.ui.PlaylistsViewModel

val playlistsViewModelModule = module {
    viewModel {
        PlaylistsViewModel(interactor = get())
    }
}
