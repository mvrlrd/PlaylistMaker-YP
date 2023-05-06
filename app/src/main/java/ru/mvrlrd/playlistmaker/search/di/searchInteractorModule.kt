package ru.mvrlrd.playlistmaker.search.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.domain.impl.TracksInteractorImpl

val searchInteractorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }
}