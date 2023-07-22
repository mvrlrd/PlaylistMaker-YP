package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistUseCaseImpl


val addPlaylistUseCaseModule = module {
    factory <AddPlaylistUseCase> {
        AddPlaylistUseCaseImpl(repository = get())
    }
}