package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.data.AddPlaylistRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistRepository

val addPlaylistRepositoryModule = module {
    single<AddPlaylistRepository> {
        AddPlaylistRepositoryImpl(dataBase = get(), converter = get())
    }
}