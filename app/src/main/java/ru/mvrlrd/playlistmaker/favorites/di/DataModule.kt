package ru.mvrlrd.playlistmaker.favorites.data.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.favorites.data.FavoriteDb
import ru.mvrlrd.playlistmaker.favorites.data.TrackConverter

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavoriteDb::class.java, "fav_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        TrackConverter()
    }
}