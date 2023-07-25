package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui.UpdatePlaylistViewModel

val updatePlaylistViewModelModule = module {
    viewModel { UpdatePlaylistViewModel(updatePlaylistUseCase = get()) }
}