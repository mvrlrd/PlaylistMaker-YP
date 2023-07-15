package ru.mvrlrd.playlistmaker.player.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.ui.PlaylistAdapterForPlayer


val playerUiModule = module {
    factory {
        PlaylistAdapterForPlayer()
    }
}