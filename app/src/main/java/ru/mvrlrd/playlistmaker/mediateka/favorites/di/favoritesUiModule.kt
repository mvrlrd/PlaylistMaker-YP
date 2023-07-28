package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesFragment

val favoritesUiModule = module {
    factory {
        FavoritesFragment()
    }
}