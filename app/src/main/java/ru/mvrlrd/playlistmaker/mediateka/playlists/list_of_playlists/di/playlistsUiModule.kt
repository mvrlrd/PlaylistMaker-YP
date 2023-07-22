package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.ui.PlaylistAdapter

val playlistsUiModule = module {
    factory { PlaylistAdapter() }
}