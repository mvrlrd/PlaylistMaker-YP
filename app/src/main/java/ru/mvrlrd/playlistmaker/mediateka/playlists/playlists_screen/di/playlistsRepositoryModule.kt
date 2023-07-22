package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.data.PlaylistRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistRepository

val playlistsRepositoryModule = module {
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(dataBase = get(), converter = get())
    }
}