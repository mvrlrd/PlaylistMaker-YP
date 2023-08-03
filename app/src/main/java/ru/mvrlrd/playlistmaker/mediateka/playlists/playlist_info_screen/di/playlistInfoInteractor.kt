package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractorImpl

val playlistInfoInteractorModule = module {
    factory <PlaylistInfoInteractor> { PlaylistInfoInteractorImpl(repository = get()) }
}