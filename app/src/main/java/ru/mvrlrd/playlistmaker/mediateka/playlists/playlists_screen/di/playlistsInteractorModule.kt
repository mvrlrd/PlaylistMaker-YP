package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.impl.PlaylistInteractorImpl

val playlistsInteractorModule = module{
    factory <PlaylistInteractor> {
        PlaylistInteractorImpl(repository = get())
    }
}