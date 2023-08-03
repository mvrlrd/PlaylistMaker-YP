package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistAdapter

val playlistsUiModule = module {
    factory { PlaylistAdapter() }
}