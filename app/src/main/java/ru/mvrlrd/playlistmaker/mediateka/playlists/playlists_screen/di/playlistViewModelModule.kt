package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistsViewModel

val playlistsViewModelModule = module {
    viewModel {
        PlaylistsViewModel(useCase = get())
    }
}
