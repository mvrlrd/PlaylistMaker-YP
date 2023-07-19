package ru.mvrlrd.playlistmaker.player.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.ui.PlaylistAdapterForPlayer
import ru.mvrlrd.playlistmaker.tools.GlideHelper


val playerUiModule = module {
    factory {
        PlaylistAdapterForPlayer()
    }

    factory {
        GlideHelper()
    }
}