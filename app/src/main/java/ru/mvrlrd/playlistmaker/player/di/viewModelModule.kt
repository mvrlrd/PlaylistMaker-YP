package ru.mvrlrd.playlistmaker.player.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.ui.PlayerViewModel

val playerViewModelModule = module {
    viewModel {
        (trackForPlayer : TrackForPlayer)   ->
        PlayerViewModel(trackForPlayer = trackForPlayer, playerInteractor = get())
    }
}