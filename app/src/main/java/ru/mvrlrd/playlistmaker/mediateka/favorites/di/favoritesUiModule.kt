package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoriteAdapter
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesFragment

val favoritesUiModule = module {
    factory {
        FavoriteAdapter()
    }

    factory {
        FavoritesFragment()
    }
}