package ru.mvrlrd.playlistmaker.player.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.impl.PlayerInteractorImpl

val playerInteractorModule = module {
    single <PlayerInteractor> {
        PlayerInteractorImpl(playerRepository = get())
    }
}