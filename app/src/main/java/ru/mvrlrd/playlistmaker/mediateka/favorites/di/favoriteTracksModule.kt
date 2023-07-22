package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.impl.FavoriteInteractorImpl

val favoritesInteractorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }
}