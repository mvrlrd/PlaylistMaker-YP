package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.FavoritesRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository

val favoritesRepositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(favoriteDb = get(), trackConverter = get())
    }
}