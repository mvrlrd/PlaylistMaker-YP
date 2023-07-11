package ru.mvrlrd.playlistmaker.search.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.search.data.TracksRepositoryImpl
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository

val searchRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get(), localStorage = get(), favoriteDb = get())
    }
}