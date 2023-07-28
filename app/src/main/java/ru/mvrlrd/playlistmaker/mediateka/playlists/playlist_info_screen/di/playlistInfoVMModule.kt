package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui.PlaylistInfoViewModel

val playlistInfoVMModule = module {
    viewModel<PlaylistInfoViewModel>{   (playerId: Long)->
        PlaylistInfoViewModel(interactor = get(), playlistId = playerId, fileUseCase = get())
    }
}
