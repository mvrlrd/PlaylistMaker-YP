package ru.mvrlrd.playlistmaker.mediateka.favorites.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.TrackConverter

val favoritesDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavoriteDb::class.java, "fav_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        TrackConverter()
    }
}