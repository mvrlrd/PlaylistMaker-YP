package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.data.PlaylistRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistRepository

val playlistsRepositoryModule = module {
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(dataBase = get(), converter = get())
    }
}