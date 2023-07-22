package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.data.PlaylistInfoRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoRepository

val playlistInfoRepositoryModule = module {
    factory<PlaylistInfoRepository> { PlaylistInfoRepositoryImpl(playlistDb = get(), converter = get()) }
}