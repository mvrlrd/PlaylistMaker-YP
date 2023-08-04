package ru.mvrlrd.playlistmaker.player.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.data.PlayerRepositoryImpl
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository

val playerRepositoryModule = module {
    single <PlayerRepository> {
        PlayerRepositoryImpl(
            playerClient = get(),
            favoriteDb = get(),
            trackConverter = get(),
            playlistConverter = get(),
            playlistDb = get()
        )
    }
}