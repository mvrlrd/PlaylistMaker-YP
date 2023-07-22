package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.impl.PlaylistInteractorImpl

val playlistsInteractorModule = module{
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(repository = get())
    }
}