package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistUseCaseImpl

val updatePlaylistUseCase = module {
    factory<UpdatePlaylistUseCase> {
        UpdatePlaylistUseCaseImpl(repository = get())
    }
}