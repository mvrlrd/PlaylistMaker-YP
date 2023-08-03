package ru.mvrlrd.playlistmaker.player.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.ui.PlayerViewModel

val playerViewModelModule = module {
    viewModel { (playerTrack: PlayerTrack) ->
        PlayerViewModel(playerTrack = playerTrack, playerInteractor = get(), fileUseCase = get())
    }
}