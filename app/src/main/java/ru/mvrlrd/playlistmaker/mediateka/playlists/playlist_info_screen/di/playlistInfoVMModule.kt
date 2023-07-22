package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui.PlaylistInfoViewModel

val playlistInfoVMModule = module {
    viewModel<PlaylistInfoViewModel>{   (playerId: Long)->
        PlaylistInfoViewModel(interactor = get(), playlistId = playerId)
    }
}
