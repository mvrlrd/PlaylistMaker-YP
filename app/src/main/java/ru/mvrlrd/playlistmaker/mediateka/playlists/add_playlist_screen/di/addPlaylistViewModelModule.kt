package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistViewModel


val addPlaylistViewModelModule = module {
    viewModel {
        AddPlaylistViewModel(interactor = get(), useCase = get())
    }
}