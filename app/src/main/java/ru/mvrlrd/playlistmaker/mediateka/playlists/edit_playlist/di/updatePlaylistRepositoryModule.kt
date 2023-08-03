package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.data.UpdatePlaylistRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistRepository

val updatePlaylistRepository = module {
    factory<UpdatePlaylistRepository> {
        UpdatePlaylistRepositoryImpl(database = get(), converter = get())
    }
}